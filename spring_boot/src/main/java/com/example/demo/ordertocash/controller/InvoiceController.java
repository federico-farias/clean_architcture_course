package com.example.demo.ordertocash.controller;

import com.example.demo.ordertocash.dto.InvoiceResponseDto;
import com.example.demo.ordertocash.service.InvoiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ordertocash/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @PostMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponseDto> generateInvoice(@PathVariable Long orderId) {
        InvoiceResponseDto response = invoiceService.generateInvoice(orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable Long id) {
        InvoiceResponseDto response = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByOrderId(@PathVariable Long orderId) {
        InvoiceResponseDto response = invoiceService.getInvoiceByOrderId(orderId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<InvoiceResponseDto> markAsPaid(@PathVariable Long id) {
        InvoiceResponseDto response = invoiceService.markAsPaid(id);
        return ResponseEntity.ok(response);
    }
}

