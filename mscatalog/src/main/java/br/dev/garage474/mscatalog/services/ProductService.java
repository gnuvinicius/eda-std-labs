package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.ProductCreateDto;
import br.dev.garage474.mscatalog.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProductService {
    ProductDto create(ProductCreateDto dto);

    ProductDto getById(UUID id);

    Page<ProductDto> list(Pageable pageable);

    ProductDto update(UUID id, ProductCreateDto dto);

    void delete(UUID id);
}

