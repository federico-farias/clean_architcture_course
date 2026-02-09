package com.example.demo.ordertocash.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class OrderItemResponseDto {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal discount;
    private BigDecimal lineTotal;
}

