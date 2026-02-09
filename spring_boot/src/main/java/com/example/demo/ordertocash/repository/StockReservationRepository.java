package com.example.demo.ordertocash.repository;

import com.example.demo.ordertocash.entity.StockReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StockReservationRepository extends JpaRepository<StockReservation, Long> {
    List<StockReservation> findByOrderId(Long orderId);
    List<StockReservation> findByProductIdAndStatus(Long productId, String status);
    List<StockReservation> findByStatusAndExpiresAtBefore(String status, LocalDateTime dateTime);
}

