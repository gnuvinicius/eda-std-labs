package br.dev.garage474.msdelivery.services;

import br.dev.garage474.msdelivery.dtos.CreateCustomerRequest;
import br.dev.garage474.msdelivery.dtos.CustomerResponse;
import br.dev.garage474.msdelivery.models.Customer;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Mapper responsible for converting between Customer DTOs and the Customer entity.
 */
@Component
public class CustomerMapper {

    /**
     * Converts a {@link CreateCustomerRequest} to a new {@link Customer} entity.
     *
     * @param request the creation request DTO
     * @return a new Customer entity (without id, timestamps are set via @PrePersist)
     */
    public Customer toEntity(CreateCustomerRequest request) {
        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(request.name());
        customer.setEmail(request.email().toLowerCase().trim());
        customer.setPhone(request.phone());
        customer.setDocument(request.document().replaceAll("[^0-9]", ""));
        return customer;
    }

    /**
     * Converts a {@link Customer} entity to a {@link CustomerResponse} DTO.
     *
     * @param customer the customer entity
     * @return the response DTO
     */
    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getDocument(),
                customer.getCreatedAt(),
                customer.getUpdatedAt()
        );
    }
}

