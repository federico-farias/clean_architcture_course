package com.example.demo.ordertocash.service;

import com.example.demo.ordertocash.dto.InvoiceResponseDto;
import com.example.demo.ordertocash.entity.Invoice;
import com.example.demo.ordertocash.entity.Order;
import com.example.demo.ordertocash.repository.InvoiceRepository;
import com.example.demo.ordertocash.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;

    public InvoiceService(InvoiceRepository invoiceRepository, OrderRepository orderRepository) {
        this.invoiceRepository = invoiceRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public InvoiceResponseDto generateInvoice(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado con ID: " + orderId));

        // Validar que el pedido esté confirmado
        if (!"CONFIRMED".equals(order.getStatus()) && !"SHIPPED".equals(order.getStatus())
                && !"DELIVERED".equals(order.getStatus())) {
            throw new RuntimeException("Solo se puede generar factura para pedidos confirmados, enviados o entregados");
        }

        // Verificar que no exista factura previa
        if (invoiceRepository.findByOrderId(orderId).isPresent()) {
            throw new RuntimeException("Ya existe una factura para el pedido: " + orderId);
        }

        Invoice invoice = new Invoice();
        invoice.setOrder(order);
        invoice.setInvoiceNumber(generateInvoiceNumber());
        invoice.setSubtotal(order.getSubtotal().subtract(order.getDiscountAmount()));
        invoice.setTaxAmount(order.getTaxAmount());
        invoice.setTotal(order.getTotal());
        invoice.setTaxRegion(order.getCustomer().getRegion());
        invoice.setTaxRate(getTaxRateByRegion(order.getCustomer().getRegion()));

        invoice = invoiceRepository.save(invoice);

        return toResponseDto(invoice);
    }

    private String generateInvoiceNumber() {
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uniquePart = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "INV-" + datePart + "-" + uniquePart;
    }

    private BigDecimal getTaxRateByRegion(String region) {
        return switch (region) {
            case "EU" -> new BigDecimal("21.00");
            case "LATAM" -> new BigDecimal("16.00");
            case "ASIA" -> new BigDecimal("10.00");
            default -> new BigDecimal("8.00");
        };
    }

    @Transactional
    public InvoiceResponseDto markAsPaid(Long invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + invoiceId));

        if (!"PENDING".equals(invoice.getStatus())) {
            throw new RuntimeException("Solo se pueden marcar como pagadas las facturas pendientes");
        }

        invoice.setStatus("PAID");
        invoice.setPaidAt(LocalDateTime.now());
        invoice = invoiceRepository.save(invoice);

        return toResponseDto(invoice);
    }

    public InvoiceResponseDto getInvoiceByOrderId(Long orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada para el pedido: " + orderId));
        return toResponseDto(invoice);
    }

    public InvoiceResponseDto getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Factura no encontrada con ID: " + id));
        return toResponseDto(invoice);
    }

    private InvoiceResponseDto toResponseDto(Invoice invoice) {
        InvoiceResponseDto dto = new InvoiceResponseDto();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setOrderId(invoice.getOrder().getId());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setTaxAmount(invoice.getTaxAmount());
        dto.setTotal(invoice.getTotal());
        dto.setTaxRegion(invoice.getTaxRegion());
        dto.setTaxRate(invoice.getTaxRate());
        dto.setStatus(invoice.getStatus());
        dto.setIssuedAt(invoice.getIssuedAt());
        dto.setPaidAt(invoice.getPaidAt());
        return dto;
    }
}

