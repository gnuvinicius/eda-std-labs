package br.dev.garage474.msestoque.infrastructure.repository;

import br.dev.garage474.msestoque.domain.Produto;
import br.dev.garage474.msestoque.domain.ProdutoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class JpaProdutoRepository implements ProdutoRepository {

    private final SpringDataProdutoRepository repository;

    public JpaProdutoRepository(SpringDataProdutoRepository repository) {
        this.repository = repository;
    }

    @Override
    public java.util.List<Produto> findAllByTenant(java.util.UUID tenantId) {
        return repository.findByTenantId(tenantId).stream().map(ProdutoEntity::toDomain).toList();
    }

    @Override
    public Optional<Produto> findByIdAndTenant(java.util.UUID id, java.util.UUID tenantId) {
        return repository.findByIdAndTenantId(id, tenantId).map(ProdutoEntity::toDomain);
    }

    @Override
    @Transactional
    public Produto save(Produto produto) {
        ProdutoEntity e = ProdutoEntity.fromDomain(produto);
        ProdutoEntity saved = repository.save(e);
        return saved.toDomain();
    }

    @Override
    @Transactional
    public void deleteByIdAndTenant(java.util.UUID id, java.util.UUID tenantId) {
        repository.deleteByIdAndTenantId(id, tenantId);
    }
}
