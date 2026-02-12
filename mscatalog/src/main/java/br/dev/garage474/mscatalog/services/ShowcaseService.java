package br.dev.garage474.mscatalog.services;

import br.dev.garage474.mscatalog.dto.ShowcaseDto;
import br.dev.garage474.mscatalog.dto.ShowcaseFilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ShowcaseService {
    Page<ShowcaseDto> list(UUID tenantId, ShowcaseFilterDto filter, Pageable pageable);

    ShowcaseDto getById(UUID tenantId, UUID id);
}

