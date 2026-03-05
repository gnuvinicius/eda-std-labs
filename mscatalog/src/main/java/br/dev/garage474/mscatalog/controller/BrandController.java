package br.dev.garage474.mscatalog.controller;

import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.dev.garage474.mscatalog.dto.BrandDto;
import br.dev.garage474.mscatalog.services.BrandService;

@RestController
@RequestMapping("/api/v1/brands")
@Validated
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    public ResponseEntity<Page<BrandDto>> list(@ParameterObject Pageable pageable) {
        Page<BrandDto> page = brandService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandDto> get(@PathVariable UUID id) {
        BrandDto dto = brandService.getById(id);
        return ResponseEntity.ok(dto);
    }

}
