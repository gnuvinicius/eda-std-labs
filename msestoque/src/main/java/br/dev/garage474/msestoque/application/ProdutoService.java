package br.dev.garage474.msestoque.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.garage474.msestoque.domain.Produto;
import br.dev.garage474.msestoque.domain.ProdutoRepository;
import br.dev.garage474.msestoque.config.TenantContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<Produto> list() {
        UUID tenant = TenantContext.getTenantId();
        return repository.findAllByTenant(tenant);
    }

    @Transactional(readOnly = true)
    public Optional<Produto> get(UUID id) {
        UUID tenant = TenantContext.getTenantId();
        return repository.findByIdAndTenant(id, tenant);
    }

    @Transactional
    public Produto create(Produto produto) {
        produto.setId(null);
        produto.setTenantId(TenantContext.getTenantId());
        return repository.save(produto);
    }

    @Transactional
    public Optional<Produto> update(UUID id, Produto produto) {
        UUID tenant = TenantContext.getTenantId();
        return repository.findByIdAndTenant(id, tenant).map(existing -> {
            existing.setNome(produto.getNome());
            existing.setDescricao(produto.getDescricao());
            existing.setPreco(produto.getPreco());
            return repository.save(existing);
        });
    }

    @Transactional
    public boolean delete(UUID id) {
        UUID tenant = TenantContext.getTenantId();
        if (repository.findByIdAndTenant(id, tenant).isPresent()) {
            repository.deleteByIdAndTenant(id, tenant);
            return true;
        }
        return false;
    }
}
