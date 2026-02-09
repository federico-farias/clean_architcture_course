package com.example.demo.ordertocash.controller;

import com.example.demo.ordertocash.dto.CreateOrderDto;
import com.example.demo.ordertocash.dto.OrderResponseDto;
import com.example.demo.ordertocash.dto.UpdateOrderStatusDto;
import com.example.demo.ordertocash.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordertocash/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        OrderResponseDto response = orderService.createOrder(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long id) {
        OrderResponseDto response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByCustomerId(@PathVariable Long customerId) {
        List<OrderResponseDto> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @PostMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDto> confirmOrder(
            @PathVariable Long id,
            @RequestParam(defaultValue = "SYSTEM") String updatedBy) {
        OrderResponseDto response = orderService.confirmOrder(id, updatedBy);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusDto dto) {
        OrderResponseDto response = orderService.updateOrderStatus(id, dto);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDto> cancelOrder(
            @PathVariable Long id,
            @RequestParam(defaultValue = "SYSTEM") String updatedBy) {
        OrderResponseDto response = orderService.cancelOrder(id, updatedBy);
        return ResponseEntity.ok(response);
    }
}
