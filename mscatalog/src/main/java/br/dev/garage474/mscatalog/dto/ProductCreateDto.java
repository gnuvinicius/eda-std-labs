package br.dev.garage474.mscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateDto {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private UUID brandId;

    @NotNull
    private UUID categoryId;

    @NotBlank
    private String slug;
}

