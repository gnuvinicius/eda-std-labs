package br.dev.garage474.mscatalog.dto;

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
public class ShowcaseDto {
    private UUID id;
    private String title;
    private String description;
    private List<ProductDto> products;

    /**
     * Creates a ShowcaseDto with custom values.
     * Note: Showcase is not a persistent entity, so this is a factory method.
     *
     * @param id the showcase id
     * @param title the showcase title
     * @param description the showcase description
     * @param products the list of products
     * @return the created ShowcaseDto
     */
    public static ShowcaseDto create(UUID id, String title, String description, List<ProductDto> products) {
        return ShowcaseDto.builder()
                .id(id)
                .title(title)
                .description(description)
                .products(products)
                .build();
    }
}

