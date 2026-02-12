package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.ProductCreateDto;
import br.dev.garage474.mscatalog.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductDto create(UUID tenantId, ProductCreateDto dto);

    ProductDto getById(UUID tenantId, UUID id);

    Page<ProductDto> list(UUID tenantId, Pageable pageable);

    ProductDto update(UUID tenantId, UUID id, ProductCreateDto dto);

    void delete(UUID tenantId, UUID id);
}

