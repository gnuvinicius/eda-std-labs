package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.dto.CategoryCreateDto;
import br.dev.garage474.mscatalog.dto.CategoryDto;
import br.dev.garage474.mscatalog.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {

    private static final Logger log = LoggerFactory.getLogger(CategoryController.class);

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@RequestHeader("tenantId") UUID tenantId, @Valid @RequestBody CategoryCreateDto dto) {
        CategoryDto created = categoryService.create(tenantId, dto);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> list(@RequestHeader("tenantId") UUID tenantId, Pageable pageable) {
        Page<CategoryDto> page = categoryService.list(tenantId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> get(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        CategoryDto dto = categoryService.getById(tenantId, id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id, @Valid @RequestBody CategoryCreateDto dto) {
        CategoryDto updated = categoryService.update(tenantId, id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        categoryService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}

