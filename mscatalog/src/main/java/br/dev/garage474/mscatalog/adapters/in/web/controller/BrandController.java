package br.dev.garage474.mscatalog.adapters.in.web.controller;

import java.net.URI;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.garage474.mscatalog.dto.BrandCreateDto;
import br.dev.garage474.mscatalog.dto.BrandDto;
import br.dev.garage474.mscatalog.services.BrandService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @PostMapping
    public ResponseEntity<BrandDto> create(@Valid @RequestBody BrandCreateDto dto) {
        BrandDto created = brandService.create(dto);
        return ResponseEntity.created(URI.create("/api/v1/brands/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<BrandDto>> list(Pageable pageable) {
        Page<BrandDto> page = brandService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> get(@PathVariable UUID id) {
        BrandDto dto = brandService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BrandDto> update(@PathVariable UUID id, @Valid @RequestBody BrandCreateDto dto) {
        BrandDto updated = brandService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        brandService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

