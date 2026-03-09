package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID id;
    private String name;
    private String description;
    private UUID brandId;
    private BrandDto brand;
    private UUID categoryId;
    private CategoryDto category;
    private List<ProductVariantDto> variants;
    private String slug;

    /**
     * Converts a Product entity to a ProductDto.
     *
     * @param product the product entity to convert
     * @return the converted ProductDto
     */
    public static ProductDto toDto(Product product) {
        if (product == null) {
            return null;
        }
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
//                .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                .brand(product.getBrand() != null ? BrandDto.toDto(product.getBrand()) : null)
//                .categoryId(product.getCategory() != null ? product.getCategory().getId() : null)
                .category(product.getCategory() != null ? CategoryDto.toDto(product.getCategory()) : null)
                .variants(product.getVariants() != null ?
                        product.getVariants().stream()
                                .map(ProductVariantDto::toDto)
                                .toList() : null)
                .slug(product.getSlug())
                .build();
    }
}

