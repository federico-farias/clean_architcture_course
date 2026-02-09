package com.example.demo.ordertocash.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDto {
    @NotNull(message = "El ID del cliente es obligatorio")
    private Long customerId;

    @NotEmpty(message = "El pedido debe tener al menos un item")
    @Valid
    private List<OrderItemDto> items;

    private String promoCode;
}

