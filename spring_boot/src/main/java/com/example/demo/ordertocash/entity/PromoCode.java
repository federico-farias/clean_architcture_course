package com.example.demo.ordertocash.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promo_codes")
@Data
public class PromoCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String discountType; // PERCENTAGE, FIXED_AMOUNT

    @Column(nullable = false)
    private BigDecimal discountValue;

    private BigDecimal minimumOrderAmount;

    private Integer maxUsages;

    private Integer currentUsages = 0;

    @Column(nullable = false)
    private LocalDateTime validFrom;

    @Column(nullable = false)
    private LocalDateTime validUntil;

    private Boolean active = true;
}

