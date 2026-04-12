package br.dev.garage474.msdelivery.resources.dtos;

import br.dev.garage474.msdelivery.models.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO representing a registered customer.
 */
@AllArgsConstructor
@Getter
public class CustomerResponse {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private String document;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CustomerResponse toResponse(Customer customer) {
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
