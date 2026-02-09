package com.example.demo.ordertocash.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private Long customerId;
    private String customerName;
    private List<OrderItemResponseDto> items;
    private String status;
    private String promoCode;
    private BigDecimal subtotal;
    private BigDecimal discountAmount;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private LocalDateTime createdAt;
}

