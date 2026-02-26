package br.dev.garage474.mscatalog.adapters.in.web.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.dev.garage474.mscatalog.dto.ShowcaseDto;
import br.dev.garage474.mscatalog.dto.ShowcaseFilterDto;
import br.dev.garage474.mscatalog.services.ShowcaseService;

@RestController
@RequestMapping("/api/v1/showcases")
@Validated
public class ShowcaseController {

    private final ShowcaseService showcaseService;

    public ShowcaseController(ShowcaseService showcaseService) {
        this.showcaseService = showcaseService;
    }

    @GetMapping
    public ResponseEntity<Page<ShowcaseDto>> list(@RequestHeader("tenantId") UUID tenantId,
                                                  @RequestParam(required = false) String q,
                                                  @RequestParam(required = false) UUID brandId,
                                                  @RequestParam(required = false) UUID categoryId,
                                                  @RequestParam(required = false) String platform,
                                                  Pageable pageable) {
        ShowcaseFilterDto filter = ShowcaseFilterDto.builder()
                .q(q)
                .brandId(brandId)
                .categoryId(categoryId)
                .platform(platform)
                .build();
        Page<ShowcaseDto> page = showcaseService.list(tenantId, filter, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShowcaseDto> get(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        ShowcaseDto dto = showcaseService.getById(tenantId, id);
        return ResponseEntity.ok(dto);
    }
}
