package br.dev.garage474.msestoque.infrastructure.controller;

import br.dev.garage474.msestoque.application.ProdutoService;
import br.dev.garage474.msestoque.domain.Produto;
import br.dev.garage474.msestoque.infrastructure.controller.dto.ProdutoDto;
import br.dev.garage474.msestoque.infrastructure.controller.dto.ProdutoMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {

    private final ProdutoService service;
    private final ProdutoMapper mapper;

    public ProdutosController(ProdutoService service, ProdutoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> list() {
        return ResponseEntity.ok(service.list().stream().map(mapper::toDto).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoDto> get(@PathVariable UUID id) {
        return service.get(id).map(mapper::toDto).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ProdutoDto> create(@RequestBody ProdutoDto produtoDto) {
        // tenant must be set in TenantContext before calling
        Produto produto = mapper.toDomain(produtoDto);
        Produto created = service.create(produto);
        URI location = URI.create("/produtos/" + created.getId());
        return ResponseEntity.created(location).body(mapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoDto> update(@PathVariable UUID id, @RequestBody ProdutoDto produtoDto) {
        Produto produto = mapper.toDomain(produtoDto);
        return service.update(id, produto).map(mapper::toDto).map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean removed = service.delete(id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
