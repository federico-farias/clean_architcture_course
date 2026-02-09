package com.example.demo.ordertocash.service;

import com.example.demo.ordertocash.entity.Order;
import com.example.demo.ordertocash.entity.Product;
import com.example.demo.ordertocash.entity.StockReservation;
import com.example.demo.ordertocash.repository.OrderRepository;
import com.example.demo.ordertocash.repository.ProductRepository;
import com.example.demo.ordertocash.repository.StockReservationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockReservationService {

    private final StockReservationRepository stockReservationRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public StockReservationService(StockReservationRepository stockReservationRepository,
                                   OrderRepository orderRepository,
                                   ProductRepository productRepository) {
        this.stockReservationRepository = stockReservationRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    /**
     * Tarea programada para liberar reservas expiradas
     * Se ejecuta cada 5 minutos
     */
    @Scheduled(fixedRate = 300000) // 5 minutos
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();

        // Buscar reservas expiradas
        List<StockReservation> expiredReservations = stockReservationRepository
                .findByStatusAndExpiresAtBefore("RESERVED", now);

        for (StockReservation reservation : expiredReservations) {
            reservation.setStatus("RELEASED");
            stockReservationRepository.save(reservation);

            // Actualizar estado del pedido si todas sus reservas están liberadas
            Order order = reservation.getOrder();
            if ("PENDING".equals(order.getStatus())) {
                order.setStatus("CANCELLED");
                order.setUpdatedBy("SYSTEM");
                orderRepository.save(order);
            }
        }
    }

    public int getReservedQuantity(Long productId) {
        return stockReservationRepository
                .findByProductIdAndStatus(productId, "RESERVED")
                .stream()
                .mapToInt(StockReservation::getReservedQuantity)
                .sum();
    }

    public int getAvailableStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        int reserved = getReservedQuantity(productId);
        return product.getStockQuantity() - reserved;
    }
}

