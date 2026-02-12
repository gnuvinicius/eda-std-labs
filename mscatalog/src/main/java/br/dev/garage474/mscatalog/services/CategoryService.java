package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.CategoryCreateDto;
import br.dev.garage474.mscatalog.dto.CategoryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {
    CategoryDto create(UUID tenantId, CategoryCreateDto dto);

    CategoryDto getById(UUID tenantId, UUID id);

    Page<CategoryDto> list(UUID tenantId, Pageable pageable);

    CategoryDto update(UUID tenantId, UUID id, CategoryCreateDto dto);

    void delete(UUID tenantId, UUID id);
}

