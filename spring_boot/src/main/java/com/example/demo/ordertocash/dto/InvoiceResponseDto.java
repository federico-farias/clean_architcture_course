package com.example.demo.ordertocash.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class InvoiceResponseDto {
    private Long id;
    private String invoiceNumber;
    private Long orderId;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal total;
    private String taxRegion;
    private BigDecimal taxRate;
    private String status;
    private LocalDateTime issuedAt;
    private LocalDateTime paidAt;
}

