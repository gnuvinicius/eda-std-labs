package br.dev.garage474.mscatalog.controller;

import br.dev.garage474.mscatalog.dto.CategoryDto;
import br.dev.garage474.mscatalog.services.CategoryService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
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
}
