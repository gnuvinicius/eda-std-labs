package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.AttributeValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttributeValueDto {

    private UUID id;
    private String value;
    private AttributeDto attribute;

    /**
     * Converts an AttributeValue entity to an AttributeValueDto.
     *
     * @param attributeValue the attribute value entity to convert
     * @return the converted AttributeValueDto
     */
    public static AttributeValueDto toDto(AttributeValue attributeValue) {
        if (attributeValue == null) {
            return null;
        }
        return AttributeValueDto.builder()
                .id(attributeValue.getId())
                .value(attributeValue.getValue())
                .attribute(AttributeDto.toDto(attributeValue.getAttribute()))
                .build();
    }
}
