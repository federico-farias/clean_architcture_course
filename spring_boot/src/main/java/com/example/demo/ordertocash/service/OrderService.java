package com.example.demo.ordertocash.service;

import com.example.demo.ordertocash.dto.*;
import com.example.demo.ordertocash.entity.*;
import com.example.demo.ordertocash.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final PromoCodeRepository promoCodeRepository;
    private final StockReservationRepository stockReservationRepository;

    // Timeout de reserva en minutos
    private static final int RESERVATION_TIMEOUT_MINUTES = 15;

    public OrderService(OrderRepository orderRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        PromoCodeRepository promoCodeRepository,
                        StockReservationRepository stockReservationRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.promoCodeRepository = promoCodeRepository;
        this.stockReservationRepository = stockReservationRepository;
    }

    @Transactional
    public OrderResponseDto createOrder(CreateOrderDto dto) {
        // Obtener cliente
        Customer customer = customerRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getCustomerId()));

        Order order = new Order();
        order.setCustomer(customer);
        order.setPromoCode(dto.getPromoCode());
        order.setReservationExpiry(LocalDateTime.now().plusMinutes(RESERVATION_TIMEOUT_MINUTES));

        BigDecimal subtotal = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        // Procesar items
        for (OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + itemDto.getProductId()));

            if (!product.getActive()) {
                throw new RuntimeException("El producto " + product.getName() + " no está activo");
            }

            // Validar stock disponible
            int availableStock = getAvailableStock(product);
            if (availableStock < itemDto.getQuantity()) {
                throw new RuntimeException("Stock insuficiente para " + product.getName() +
                        ". Disponible: " + availableStock + ", Solicitado: " + itemDto.getQuantity());
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDto.getQuantity());
            orderItem.setUnitPrice(product.getPrice());

            // Calcular descuento por volumen (lógica de negocio en el servicio)
            BigDecimal volumeDiscount = calculateVolumeDiscount(itemDto.getQuantity(), product.getPrice());
            orderItem.setDiscount(volumeDiscount);

            // Calcular total de línea
            BigDecimal lineTotal = product.getPrice()
                    .multiply(BigDecimal.valueOf(itemDto.getQuantity()))
                    .subtract(volumeDiscount);
            orderItem.setLineTotal(lineTotal);

            subtotal = subtotal.add(lineTotal);
            orderItems.add(orderItem);
        }

        order.setItems(orderItems);
        order.setSubtotal(subtotal);

        // Aplicar código promocional
        BigDecimal discountAmount = BigDecimal.ZERO;
        if (dto.getPromoCode() != null && !dto.getPromoCode().isEmpty()) {
            discountAmount = applyPromoCode(dto.getPromoCode(), subtotal, customer.getTier());
        }

        // Aplicar descuento por tier del cliente
        BigDecimal tierDiscount = calculateTierDiscount(customer.getTier(), subtotal);
        discountAmount = discountAmount.add(tierDiscount);

        order.setDiscountAmount(discountAmount);

        // Calcular impuestos según región
        BigDecimal taxableAmount = subtotal.subtract(discountAmount);
        BigDecimal taxAmount = calculateTax(customer.getRegion(), taxableAmount);
        order.setTaxAmount(taxAmount);

        // Total final
        BigDecimal total = taxableAmount.add(taxAmount);
        order.setTotal(total);

        order = orderRepository.save(order);

        // Crear reservas de stock
        for (OrderItem item : order.getItems()) {
            createStockReservation(order, item.getProduct(), item.getQuantity());
        }

        return toResponseDto(order);
    }

    private int getAvailableStock(Product product) {
        // Stock físico menos reservas activas
        int reservedQuantity = stockReservationRepository
                .findByProductIdAndStatus(product.getId(), "RESERVED")
                .stream()
                .mapToInt(StockReservation::getReservedQuantity)
                .sum();
        return product.getStockQuantity() - reservedQuantity;
    }

    private BigDecimal calculateVolumeDiscount(int quantity, BigDecimal unitPrice) {
        // Descuento por volumen:
        // 10-19 unidades: 5%
        // 20-49 unidades: 10%
        // 50+ unidades: 15%
        BigDecimal discountPercentage;
        if (quantity >= 50) {
            discountPercentage = new BigDecimal("0.15");
        } else if (quantity >= 20) {
            discountPercentage = new BigDecimal("0.10");
        } else if (quantity >= 10) {
            discountPercentage = new BigDecimal("0.05");
        } else {
            return BigDecimal.ZERO;
        }

        return unitPrice.multiply(BigDecimal.valueOf(quantity)).multiply(discountPercentage)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal applyPromoCode(String code, BigDecimal subtotal, String customerTier) {
        PromoCode promoCode = promoCodeRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Código promocional no válido: " + code));

        // Validaciones de código promocional
        LocalDateTime now = LocalDateTime.now();
        if (!promoCode.getActive()) {
            throw new RuntimeException("El código promocional está inactivo");
        }
        if (now.isBefore(promoCode.getValidFrom()) || now.isAfter(promoCode.getValidUntil())) {
            throw new RuntimeException("El código promocional ha expirado o aún no es válido");
        }
        if (promoCode.getMaxUsages() != null && promoCode.getCurrentUsages() >= promoCode.getMaxUsages()) {
            throw new RuntimeException("El código promocional ha alcanzado su límite de usos");
        }
        if (promoCode.getMinimumOrderAmount() != null && subtotal.compareTo(promoCode.getMinimumOrderAmount()) < 0) {
            throw new RuntimeException("El pedido no alcanza el monto mínimo de " + promoCode.getMinimumOrderAmount());
        }

        // Incrementar uso del código
        promoCode.setCurrentUsages(promoCode.getCurrentUsages() + 1);
        promoCodeRepository.save(promoCode);

        // Calcular descuento
        if ("PERCENTAGE".equals(promoCode.getDiscountType())) {
            return subtotal.multiply(promoCode.getDiscountValue().divide(BigDecimal.valueOf(100)))
                    .setScale(2, RoundingMode.HALF_UP);
        } else {
            return promoCode.getDiscountValue();
        }
    }

    private BigDecimal calculateTierDiscount(String tier, BigDecimal subtotal) {
        // Descuento por tier:
        // STANDARD: 0%
        // PREMIUM: 5%
        // VIP: 10%
        BigDecimal discountPercentage = switch (tier) {
            case "VIP" -> new BigDecimal("0.10");
            case "PREMIUM" -> new BigDecimal("0.05");
            default -> BigDecimal.ZERO;
        };

        return subtotal.multiply(discountPercentage).setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calculateTax(String region, BigDecimal taxableAmount) {
        // Impuestos por región:
        // US: 8%
        // EU: 21%
        // LATAM: 16%
        // ASIA: 10%
        BigDecimal taxRate = switch (region) {
            case "EU" -> new BigDecimal("0.21");
            case "LATAM" -> new BigDecimal("0.16");
            case "ASIA" -> new BigDecimal("0.10");
            default -> new BigDecimal("0.08"); // US por defecto
        };

        return taxableAmount.multiply(taxRate).setScale(2, RoundingMode.HALF_UP);
    }

    private void createStockReservation(Order order, Product product, int quantity) {
        StockReservation reservation = new StockReservation();
        reservation.setOrder(order);
        reservation.setProduct(product);
        reservation.setReservedQuantity(quantity);
        reservation.setExpiresAt(order.getReservationExpiry());
        stockReservationRepository.save(reservation);
    }

    @Transactional
    public OrderResponseDto confirmOrder(Long orderId, String updatedBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + orderId));

        // Validar estado
        if (!"PENDING".equals(order.getStatus())) {
            throw new RuntimeException("Solo se pueden confirmar pedidos en estado PENDING. Estado actual: " + order.getStatus());
        }

        // Verificar que la reserva no haya expirado
        if (LocalDateTime.now().isAfter(order.getReservationExpiry())) {
            throw new RuntimeException("La reserva del pedido ha expirado");
        }

        // Confirmar reservas de stock y decrementar inventario
        List<StockReservation> reservations = stockReservationRepository.findByOrderId(orderId);
        for (StockReservation reservation : reservations) {
            Product product = reservation.getProduct();
            product.setStockQuantity(product.getStockQuantity() - reservation.getReservedQuantity());
            productRepository.save(product);

            reservation.setStatus("CONFIRMED");
            stockReservationRepository.save(reservation);
        }

        order.setStatus("CONFIRMED");
        order.setUpdatedBy(updatedBy);
        order = orderRepository.save(order);

        return toResponseDto(order);
    }

    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, UpdateOrderStatusDto dto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + orderId));

        String currentStatus = order.getStatus();
        String newStatus = dto.getStatus();

        // Validar transiciones de estado permitidas
        if (!isValidStatusTransition(currentStatus, newStatus)) {
            throw new RuntimeException("Transición de estado no permitida: " + currentStatus + " -> " + newStatus);
        }

        order.setStatus(newStatus);
        order.setUpdatedBy(dto.getUpdatedBy());
        order = orderRepository.save(order);

        return toResponseDto(order);
    }

    private boolean isValidStatusTransition(String currentStatus, String newStatus) {
        // Reglas de transición:
        // PENDING -> CONFIRMED, CANCELLED
        // CONFIRMED -> SHIPPED, CANCELLED
        // SHIPPED -> DELIVERED
        // DELIVERED, CANCELLED -> (no se puede cambiar)
        return switch (currentStatus) {
            case "PENDING" -> "CONFIRMED".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "CONFIRMED" -> "SHIPPED".equals(newStatus) || "CANCELLED".equals(newStatus);
            case "SHIPPED" -> "DELIVERED".equals(newStatus);
            default -> false;
        };
    }

    @Transactional
    public OrderResponseDto cancelOrder(Long orderId, String updatedBy) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + orderId));

        String currentStatus = order.getStatus();

        // Política de cancelación
        if ("SHIPPED".equals(currentStatus) || "DELIVERED".equals(currentStatus)) {
            throw new RuntimeException("No se puede cancelar un pedido en estado: " + currentStatus);
        }

        // Liberar reservas de stock
        List<StockReservation> reservations = stockReservationRepository.findByOrderId(orderId);
        for (StockReservation reservation : reservations) {
            if ("CONFIRMED".equals(reservation.getStatus())) {
                // Devolver stock si ya estaba confirmado
                Product product = reservation.getProduct();
                product.setStockQuantity(product.getStockQuantity() + reservation.getReservedQuantity());
                productRepository.save(product);
            }
            reservation.setStatus("RELEASED");
            stockReservationRepository.save(reservation);
        }

        order.setStatus("CANCELLED");
        order.setUpdatedBy(updatedBy);
        order = orderRepository.save(order);

        return toResponseDto(order);
    }

    public OrderResponseDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + id));
        return toResponseDto(order);
    }

    public List<OrderResponseDto> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    private OrderResponseDto toResponseDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setCustomerId(order.getCustomer().getId());
        dto.setCustomerName(order.getCustomer().getName());
        dto.setStatus(order.getStatus());
        dto.setPromoCode(order.getPromoCode());
        dto.setSubtotal(order.getSubtotal());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setTaxAmount(order.getTaxAmount());
        dto.setTotal(order.getTotal());
        dto.setCreatedAt(order.getCreatedAt());

        List<OrderItemResponseDto> itemDtos = order.getItems().stream()
                .map(item -> {
                    OrderItemResponseDto itemDto = new OrderItemResponseDto();
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProductName(item.getProduct().getName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setUnitPrice(item.getUnitPrice());
                    itemDto.setDiscount(item.getDiscount());
                    itemDto.setLineTotal(item.getLineTotal());
                    return itemDto;
                })
                .collect(Collectors.toList());

        dto.setItems(itemDtos);

        return dto;
    }
}

