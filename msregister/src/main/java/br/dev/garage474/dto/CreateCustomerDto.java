package br.dev.garage474.dto;

import br.dev.garage474.entity.Customer;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateCustomerDto {

    private final String name;
    private final String email;
    private final String password;

    public CreateCustomerDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public Customer toEntity(UUID tenantId) {
        return new Customer(this.name, this.email, this.password, tenantId);
    }
}
