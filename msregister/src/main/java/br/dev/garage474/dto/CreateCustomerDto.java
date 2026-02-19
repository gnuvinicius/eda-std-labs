package br.dev.garage474.dto;

import br.dev.garage474.entity.Customer;
import lombok.Getter;

import java.util.UUID;

@Getter
public class CreateCustomerDto {

    private String name;
    private  String email;
    private String password;

    public Customer toEntity(UUID tenantId) {
        Customer customer = new Customer();
        // Mapear os campos do DTO para a entidade
        // Exemplo:
         customer.setName(this.name);
         customer.setEmail(this.email);
         customer.setPassword(this.password);
        return customer;
    }
}
