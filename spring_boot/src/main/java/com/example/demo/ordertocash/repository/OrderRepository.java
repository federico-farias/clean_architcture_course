package com.example.demo.ordertocash.repository;

import com.example.demo.ordertocash.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.reservationExpiry < :now")
    List<Order> findExpiredReservations(LocalDateTime now);
}

