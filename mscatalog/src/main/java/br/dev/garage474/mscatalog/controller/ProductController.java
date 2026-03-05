package br.dev.garage474.mscatalog.controller;

import br.dev.garage474.mscatalog.dto.ProductDto;
import br.dev.garage474.mscatalog.services.ProductService;
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
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> list(@ParameterObject Pageable pageable) {
        Page<ProductDto> page = productService.list(pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable UUID id) {
        ProductDto dto = productService.getById(id);
        return ResponseEntity.ok(dto);
    }
}
