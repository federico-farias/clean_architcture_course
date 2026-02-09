package com.example.demo.ordertocash.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateOrderStatusDto {
    @NotBlank(message = "El estado es obligatorio")
    private String status;

    private String updatedBy;
}

