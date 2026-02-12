package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.Attribute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeDto {

    private UUID id;
    private String name;

    /**
     * Converts an Attribute entity to an AttributeDto.
     *
     * @param attribute the attribute entity to convert
     * @return the converted AttributeDto
     */
    public static AttributeDto toDto(Attribute attribute) {
        if (attribute == null) {
            return null;
        }
        return AttributeDto.builder()
                .id(attribute.getId())
                .name(attribute.getName())
                .build();
    }
}
