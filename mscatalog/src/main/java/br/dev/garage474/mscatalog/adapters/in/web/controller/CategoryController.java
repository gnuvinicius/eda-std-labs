package br.dev.garage474.mscatalog.adapters.in.web.controller;

import java.net.URI;
import java.util.UUID;

import org.springdoc.core.annotations.ParameterObject;
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

import br.dev.garage474.mscatalog.dto.CategoryCreateDto;
import br.dev.garage474.mscatalog.dto.CategoryDto;
import br.dev.garage474.mscatalog.services.CategoryService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> create(@Valid @RequestBody CategoryCreateDto dto) {
        CategoryDto created = categoryService.create(dto);
        return ResponseEntity.created(URI.create("/api/v1/categories/" + created.getId())).body(created);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryDto>> list(@ParameterObject Pageable pageable) {
        Page<CategoryDto> page = categoryService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable UUID id) {
        CategoryDto dto = categoryService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> update(@PathVariable UUID id, @Valid @RequestBody CategoryCreateDto dto) {
        CategoryDto updated = categoryService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
