package com.example.demo.ordertocash.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String invoiceNumber;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(nullable = false)
    private BigDecimal subtotal;

    @Column(nullable = false)
    private BigDecimal taxAmount;

    @Column(nullable = false)
    private BigDecimal total;

    private String taxRegion;

    private BigDecimal taxRate;

    @Column(nullable = false)
    private String status; // PENDING, PAID, CANCELLED

    private LocalDateTime issuedAt;

    private LocalDateTime paidAt;

    @PrePersist
    public void prePersist() {
        this.issuedAt = LocalDateTime.now();
        this.status = "PENDING";
    }
}

