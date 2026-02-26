package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.dto.BrandCreateDto;
import br.dev.garage474.mscatalog.dto.BrandDto;
import br.dev.garage474.mscatalog.services.BrandService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BrandDto> create(@RequestHeader("tenantId") UUID tenantId, @Valid @RequestBody BrandCreateDto dto) {
        BrandDto created = brandService.create(tenantId, dto);
        return ResponseEntity.created(URI.create("/api/v1/brands/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<BrandDto>> list(@RequestHeader("tenantId") UUID tenantId, Pageable pageable) {
        Page<BrandDto> page = brandService.list(tenantId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> get(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        BrandDto dto = brandService.getById(tenantId, id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> update(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id, @Valid @RequestBody BrandCreateDto dto) {
        BrandDto updated = brandService.update(tenantId, id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        brandService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}

