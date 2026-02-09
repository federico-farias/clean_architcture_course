package com.example.demo.ordertocash.controller;

import com.example.demo.ordertocash.dto.CreateCustomerDto;
import com.example.demo.ordertocash.dto.CustomerResponseDto;
import com.example.demo.ordertocash.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordertocash/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDto> createCustomer(@Valid @RequestBody CreateCustomerDto dto) {
        CustomerResponseDto response = customerService.createCustomer(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable Long id) {
        CustomerResponseDto response = customerService.getCustomerById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getAllCustomers() {
        List<CustomerResponseDto> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @PatchMapping("/{id}/upgrade-tier")
    public ResponseEntity<CustomerResponseDto> upgradeTier(
            @PathVariable Long id,
            @RequestParam String tier) {
        CustomerResponseDto response = customerService.upgradeTier(id, tier);
        return ResponseEntity.ok(response);
    }
}

