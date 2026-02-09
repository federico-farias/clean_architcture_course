package com.example.demo.ordertocash.dto;

import lombok.Data;

@Data
public class CustomerResponseDto {
    private Long id;
    private String email;
    private String name;
    private String region;
    private String tier;
}

