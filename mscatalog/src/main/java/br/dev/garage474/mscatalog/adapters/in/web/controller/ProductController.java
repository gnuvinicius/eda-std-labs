package br.dev.garage474.mscatalog.adapters.in.web.controller;

import br.dev.garage474.mscatalog.dto.ProductCreateDto;
import br.dev.garage474.mscatalog.dto.ProductDto;
import br.dev.garage474.mscatalog.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@Validated
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@RequestHeader("tenantId") UUID tenantId, @Valid @RequestBody ProductCreateDto dto) {
        try {
            ProductDto created = productService.create(tenantId, dto);
            return ResponseEntity.created(URI.create("/api/v1/products/" + created.getId())).body(created);
        } catch (Exception e) {
            log.error("error in create product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> list(@RequestHeader("tenantId") UUID tenantId, @ParameterObject Pageable pageable) {
        Page<ProductDto> page = productService.list(tenantId, pageable);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> get(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        ProductDto dto = productService.getById(tenantId, id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id, @Valid @RequestBody ProductCreateDto dto) {
        ProductDto updated = productService.update(tenantId, id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@RequestHeader("tenantId") UUID tenantId, @PathVariable UUID id) {
        productService.delete(tenantId, id);
        return ResponseEntity.noContent().build();
    }
}
