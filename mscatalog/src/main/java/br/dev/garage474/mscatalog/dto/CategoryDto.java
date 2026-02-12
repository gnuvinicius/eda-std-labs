package br.dev.garage474.mscatalog.dto;

import br.dev.garage474.mscatalog.models.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    private UUID id;
    private String name;
    private UUID parentId;

    /**
     * Converts a Category entity to a CategoryDto.
     *
     * @param category the category entity to convert
     * @return the converted CategoryDto
     */
    public static CategoryDto toDto(Category category) {
        if (category == null) {
            return null;
        }
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .parentId(category.getParent() != null ? category.getParent().getId() : null)
                .build();
    }
}

