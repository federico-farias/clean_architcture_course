package com.example.demo.ordertocash.service;

import com.example.demo.ordertocash.dto.CreateProductDto;
import com.example.demo.ordertocash.dto.ProductResponseDto;
import com.example.demo.ordertocash.entity.Product;
import com.example.demo.ordertocash.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Transactional
    public ProductResponseDto createProduct(CreateProductDto dto) {
        // Validación mezclada en el servicio
        if (productRepository.existsBySku(dto.getSku())) {
            throw new RuntimeException("Ya existe un producto con el SKU: " + dto.getSku());
        }

        Product product = new Product();
        product.setSku(dto.getSku());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setActive(true);

        product = productRepository.save(product);

        return toResponseDto(product);
    }

    public ProductResponseDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return toResponseDto(product);
    }

    public List<ProductResponseDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProductResponseDto updateStock(Long productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        int newStock = product.getStockQuantity() + quantity;
        if (newStock < 0) {
            throw new RuntimeException("El stock no puede ser negativo. Stock actual: " + product.getStockQuantity());
        }

        product.setStockQuantity(newStock);
        product = productRepository.save(product);

        return toResponseDto(product);
    }

    @Transactional
    public void deactivateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + productId));

        product.setActive(false);
        productRepository.save(product);
    }

    private ProductResponseDto toResponseDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setSku(product.getSku());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setActive(product.getActive());
        return dto;
    }
}

