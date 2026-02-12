package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.Dimensions;
import br.dev.garage474.mscatalog.models.Money;
import br.dev.garage474.mscatalog.models.ProductVariant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantDto {

    private UUID id;
    private String skuCode;
    private String barcode;
    private Money price;
    private Money promotionalPrice;
    private Dimensions dimensions;
    private List<AttributeValueDto> attributeValue;

    /**
     * Converts a ProductVariant entity to a ProductVariantDto.
     *
     * @param variant the product variant entity to convert
     * @return the converted ProductVariantDto
     */
    public static ProductVariantDto toDto(ProductVariant variant) {
        if (variant == null) {
            return null;
        }
        List<AttributeValueDto> attributeValues = variant.getAttributeValue() != null
                ? variant.getAttributeValue().stream()
                    .map(AttributeValueDto::toDto)
                    .collect(Collectors.toList())
                : new ArrayList<>();

        return ProductVariantDto.builder()
                .id(variant.getId())
                .skuCode(variant.getSkuCode())
                .barcode(variant.getBarcode())
                .price(variant.getPrice())
                .promotionalPrice(variant.getPromotionalPrice())
                .dimensions(variant.getDimensions())
                .attributeValue(attributeValues)
                .build();
    }
}
