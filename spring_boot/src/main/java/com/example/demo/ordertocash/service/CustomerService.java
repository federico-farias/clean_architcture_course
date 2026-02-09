package com.example.demo.ordertocash.service;

import com.example.demo.ordertocash.dto.CreateCustomerDto;
import com.example.demo.ordertocash.dto.CustomerResponseDto;
import com.example.demo.ordertocash.entity.Customer;
import com.example.demo.ordertocash.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Transactional
    public CustomerResponseDto createCustomer(CreateCustomerDto dto) {
        // Validación de negocio mezclada con el servicio
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe un cliente con el email: " + dto.getEmail());
        }

        // Validación del tier
        String tier = dto.getTier();
        if (tier == null || tier.isEmpty()) {
            tier = "STANDARD";
        }
        if (!tier.equals("STANDARD") && !tier.equals("PREMIUM") && !tier.equals("VIP")) {
            throw new RuntimeException("El tier debe ser STANDARD, PREMIUM o VIP");
        }

        // Validación de región
        List<String> validRegions = List.of("US", "EU", "LATAM", "ASIA");
        if (!validRegions.contains(dto.getRegion())) {
            throw new RuntimeException("Región no válida. Debe ser: US, EU, LATAM o ASIA");
        }

        Customer customer = new Customer();
        customer.setEmail(dto.getEmail());
        customer.setName(dto.getName());
        customer.setRegion(dto.getRegion());
        customer.setTier(tier);

        customer = customerRepository.save(customer);

        return toResponseDto(customer);
    }

    public CustomerResponseDto getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
        return toResponseDto(customer);
    }

    public List<CustomerResponseDto> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public CustomerResponseDto upgradeTier(Long customerId, String newTier) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + customerId));

        // Lógica de negocio en el servicio
        if (!newTier.equals("STANDARD") && !newTier.equals("PREMIUM") && !newTier.equals("VIP")) {
            throw new RuntimeException("El tier debe ser STANDARD, PREMIUM o VIP");
        }

        // Regla: no se puede bajar de tier
        int currentTierLevel = getTierLevel(customer.getTier());
        int newTierLevel = getTierLevel(newTier);
        if (newTierLevel < currentTierLevel) {
            throw new RuntimeException("No se puede bajar de tier. Tier actual: " + customer.getTier());
        }

        customer.setTier(newTier);
        customer = customerRepository.save(customer);

        return toResponseDto(customer);
    }

    private int getTierLevel(String tier) {
        return switch (tier) {
            case "STANDARD" -> 1;
            case "PREMIUM" -> 2;
            case "VIP" -> 3;
            default -> 0;
        };
    }

    private CustomerResponseDto toResponseDto(Customer customer) {
        CustomerResponseDto dto = new CustomerResponseDto();
        dto.setId(customer.getId());
        dto.setEmail(customer.getEmail());
        dto.setName(customer.getName());
        dto.setRegion(customer.getRegion());
        dto.setTier(customer.getTier());
        return dto;
    }
}

