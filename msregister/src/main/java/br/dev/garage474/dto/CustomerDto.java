package br.dev.garage474.dto;

import br.dev.garage474.entity.Customer;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class CustomerDto {

    private UUID id;
    private String name;
    private String email;

    public static CustomerDto mapToDto(Customer saved) {
        return CustomerDto.builder()
                .id(saved.getId())
                .name(saved.getName())
                .email(saved.getEmail())
                .build();
    }
}
