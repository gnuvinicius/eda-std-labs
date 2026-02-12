package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.Brand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BrandDto {
    private UUID id;
    private String name;

    /**
     * Converts a Brand entity to a BrandDto.
     *
     * @param brand the brand entity to convert
     * @return the converted BrandDto
     */
    public static BrandDto toDto(Brand brand) {
        if (brand == null) {
            return null;
        }
        return BrandDto.builder()
                .id(brand.getId())
                .name(brand.getName())
                .build();
    }
}

