package br.dev.garage474.mscatalog.domain.repositories;

import br.dev.garage474.mscatalog.domain.entities.Attribute;
import br.dev.garage474.mscatalog.domain.entities.AttributeValue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository para o Aggregate Root Attribute no bounded context mscatalog.
 *
 * Define os contratos para operações CRUD do agregado Attribute e suas entidades filhas (AttributeValue).
 * Esta interface segue o padrão DDD onde cada Aggregate Root possui seu próprio repositório.
 *
 * Responsabilidades:
 * - Persistência de Attribute (Aggregate Root)
 * - Persistência de AttributeValue (Child Entity)
 * - Gerenciamento de atributos e seus valores possíveis
 */
public interface AttributeRepository {

    // ==================== ATTRIBUTE ====================

    /**
     * Salva um novo atributo ou atualiza um existente.
     */
    Attribute saveAttribute(Attribute attribute);

    /**
     * Obtém um atributo por ID.
     */
    Optional<Attribute> findAttributeById(UUID id);

    /**
     * Obtém todos os atributos.
     */
    List<Attribute> findAllAttributes();

    /**
     * Obtém um atributo por nome.
     */
    Optional<Attribute> findAttributeByName(String name);

    /**
     * Deleta um atributo por ID (e seus valores em cascata).
     */
    void deleteAttribute(UUID id);

    /**
     * Verifica se um atributo existe por ID.
     */
    boolean existsAttribute(UUID id);

    /**
     * Conta o total de atributos.
     */
    long countAttributes();

    // ==================== ATTRIBUTE VALUE ====================

    /**
     * Salva um novo valor de atributo ou atualiza um existente.
     */
    AttributeValue saveAttributeValue(AttributeValue attributeValue);

    /**
     * Obtém um valor de atributo por ID.
     */
    Optional<AttributeValue> findAttributeValueById(UUID id);

    /**
     * Obtém todos os valores de um atributo específico.
     */
    List<AttributeValue> findAttributeValuesByAttributeId(UUID attributeId);

    /**
     * Deleta um valor de atributo por ID.
     */
    void deleteAttributeValue(UUID id);

    /**
     * Deleta todos os valores de um atributo específico (útil ao deletar atributo).
     */
    void deleteAttributeValuesByAttributeId(UUID attributeId);

    /**
     * Verifica se um valor de atributo existe por ID.
     */
    boolean existsAttributeValue(UUID id);

    /**
     * Conta o total de valores de um atributo.
     */
    long countAttributeValuesByAttributeId(UUID attributeId);
}

