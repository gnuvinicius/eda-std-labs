package br.dev.garage474.mscatalog.domain.repository;

import br.dev.garage474.mscatalog.domain.entity.Brand;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para o Aggregate Root Brand no bounded context mscatalog.
 *
 * Define os contratos para operações CRUD do agregado Brand.
 * Esta interface segue o padrão DDD onde cada Aggregate Root possui seu próprio repositório.
 *
 * Responsabilidades:
 * - Persistência de Brand (Aggregate Root)
 * - Garantir a unicidade do nome
 * - Consultas de marca por identificadores
 */
public interface BrandRepository {

    /**
     * Salva uma nova marca ou atualiza uma existente.
     */
    Brand saveBrand(Brand brand);

    /**
     * Obtém uma marca por ID.
     */
    Optional<Brand> findBrandById(UUID id);

    /**
     * Obtém todas as marcas.
     */
    List<Brand> findAllBrands();

    /**
     * Obtém uma marca por nome.
     */
    Optional<Brand> findBrandByName(String name);

    /**
     * Deleta uma marca por ID.
     */
    void deleteBrand(UUID id);

    /**
     * Verifica se uma marca existe por ID.
     */
    boolean existsBrand(UUID id);

    /**
     * Conta o total de marcas.
     */
    long countBrands();
}

