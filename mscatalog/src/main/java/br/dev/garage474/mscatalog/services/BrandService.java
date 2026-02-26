package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.BrandCreateDto;
import br.dev.garage474.mscatalog.dto.BrandDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BrandService {
    BrandDto create(BrandCreateDto dto);

    BrandDto getById(UUID id);

    Page<BrandDto> list(Pageable pageable);

    BrandDto update(UUID id, BrandCreateDto dto);

    void delete(UUID id);
}

