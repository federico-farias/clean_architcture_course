package com.example.demo.ordertocash.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductResponseDto {
    private Long id;
    private String sku;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private Boolean active;
}

