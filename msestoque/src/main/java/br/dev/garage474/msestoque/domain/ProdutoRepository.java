package br.dev.garage474.msestoque.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProdutoRepository {

    List<Produto> findAllByTenant(UUID tenantId);

    Optional<Produto> findByIdAndTenant(UUID id, UUID tenantId);

    Produto save(Produto produto);

    void deleteByIdAndTenant(UUID id, UUID tenantId);
}
