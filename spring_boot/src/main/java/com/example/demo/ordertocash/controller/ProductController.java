package com.example.demo.ordertocash.controller;

import com.example.demo.ordertocash.dto.CreateProductDto;
import com.example.demo.ordertocash.dto.ProductResponseDto;
import com.example.demo.ordertocash.service.ProductService;
import com.example.demo.ordertocash.service.StockReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordertocash/products")
public class ProductController {

    private final ProductService productService;
    private final StockReservationService stockReservationService;

    public ProductController(ProductService productService, StockReservationService stockReservationService) {
        this.productService = productService;
        this.stockReservationService = stockReservationService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody CreateProductDto dto) {
        ProductResponseDto response = productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable Long id) {
        ProductResponseDto response = productService.getProductById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDto> updateStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        ProductResponseDto response = productService.updateStock(id, quantity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/available-stock")
    public ResponseEntity<Integer> getAvailableStock(@PathVariable Long id) {
        int availableStock = stockReservationService.getAvailableStock(id);
        return ResponseEntity.ok(availableStock);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivateProduct(@PathVariable Long id) {
        productService.deactivateProduct(id);
        return ResponseEntity.noContent().build();
    }
}

