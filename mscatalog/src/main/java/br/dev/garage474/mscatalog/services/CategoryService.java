package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.CategoryCreateDto;
import br.dev.garage474.mscatalog.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {
    CategoryDto create(CategoryCreateDto dto);

    CategoryDto getById(UUID id);

    Page<CategoryDto> list(Pageable pageable);

    CategoryDto update(UUID id, CategoryCreateDto dto);

    void delete(UUID id);
}

