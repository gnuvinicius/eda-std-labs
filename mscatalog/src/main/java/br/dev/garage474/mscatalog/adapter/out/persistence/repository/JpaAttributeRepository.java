package br.dev.garage474.mscatalog.adapter.out.persistence.repository;

import br.dev.garage474.mscatalog.adapter.out.persistence.entity.AttributeEntity;
import br.dev.garage474.mscatalog.adapter.out.persistence.entity.AttributeValueEntity;
import br.dev.garage474.mscatalog.domain.entity.Attribute;
import br.dev.garage474.mscatalog.domain.entity.AttributeValue;
import br.dev.garage474.mscatalog.domain.repository.AttributeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Implementação JPA do repositório {@link AttributeRepository}.
 *
 * Responsável pela persistência do agregado Attribute e suas entidades filhas (AttributeValue).
 */
@Repository
public class JpaAttributeRepository implements AttributeRepository {

    private final AttributeJpaRepository attributeJpaRepository;
    private final AttributeValueJpaRepository attributeValueJpaRepository;

    public JpaAttributeRepository(
            AttributeJpaRepository attributeJpaRepository,
            AttributeValueJpaRepository attributeValueJpaRepository) {
        this.attributeJpaRepository = attributeJpaRepository;
        this.attributeValueJpaRepository = attributeValueJpaRepository;
    }

    // ==================== ATTRIBUTE ====================

    @Override
    public Attribute saveAttribute(Attribute attribute) {
        AttributeEntity entity = convertAttributeToDomain(attribute);
        AttributeEntity savedEntity = attributeJpaRepository.save(entity);
        return convertAttributeToEntity(savedEntity);
    }

    @Override
    public Optional<Attribute> findAttributeById(UUID id) {
        return attributeJpaRepository.findById(id).map(this::convertAttributeToEntity);
    }

    @Override
    public List<Attribute> findAllAttributes() {
        return attributeJpaRepository.findAll().stream()
                .map(this::convertAttributeToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Attribute> findAttributeByName(String name) {
        return attributeJpaRepository.findByName(name).map(this::convertAttributeToEntity);
    }

    @Override
    public void deleteAttribute(UUID id) {
        attributeJpaRepository.deleteById(id);
    }

    @Override
    public boolean existsAttribute(UUID id) {
        return attributeJpaRepository.existsById(id);
    }

    @Override
    public long countAttributes() {
        return attributeJpaRepository.count();
    }

    // ==================== ATTRIBUTE VALUE ====================

    @Override
    public AttributeValue saveAttributeValue(AttributeValue attributeValue) {
        AttributeValueEntity entity = convertAttributeValueToDomain(attributeValue);
        AttributeValueEntity savedEntity = attributeValueJpaRepository.save(entity);
        return convertAttributeValueToEntity(savedEntity);
    }

    @Override
    public Optional<AttributeValue> findAttributeValueById(UUID id) {
        return attributeValueJpaRepository.findById(id).map(this::convertAttributeValueToEntity);
    }

    @Override
    public List<AttributeValue> findAttributeValuesByAttributeId(UUID attributeId) {
        return attributeValueJpaRepository.findByAttributeEntityId(attributeId).stream()
                .map(this::convertAttributeValueToEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAttributeValue(UUID id) {
        attributeValueJpaRepository.deleteById(id);
    }

    @Override
    public void deleteAttributeValuesByAttributeId(UUID attributeId) {
        attributeValueJpaRepository.deleteByAttributeEntityId(attributeId);
    }

    @Override
    public boolean existsAttributeValue(UUID id) {
        return attributeValueJpaRepository.existsById(id);
    }

    @Override
    public long countAttributeValuesByAttributeId(UUID attributeId) {
        return attributeValueJpaRepository.countByAttributeEntityId(attributeId);
    }

    // ==================== CONVERSION METHODS ====================

    private Attribute convertAttributeToEntity(AttributeEntity entity) {
        Attribute attribute = new Attribute();
        attribute.setId(entity.getId());
        attribute.setName(entity.getName());
        return attribute;
    }

    private AttributeEntity convertAttributeToDomain(Attribute attribute) {
        AttributeEntity entity = new AttributeEntity();
        entity.setId(attribute.getId());
        entity.setName(attribute.getName());
        return entity;
    }

    private AttributeValue convertAttributeValueToEntity(AttributeValueEntity entity) {
        AttributeValue attributeValue = new AttributeValue();
        attributeValue.setId(entity.getId());
        attributeValue.setValue(entity.getValue());

        if (entity.getAttributeEntity() != null) {
            attributeValue.setAttribute(convertAttributeToEntity(entity.getAttributeEntity()));
        }

        return attributeValue;
    }

    private AttributeValueEntity convertAttributeValueToDomain(AttributeValue attributeValue) {
        AttributeValueEntity entity = new AttributeValueEntity();
        entity.setId(attributeValue.getId());
        entity.setValue(attributeValue.getValue());

        if (attributeValue.getAttribute() != null && attributeValue.getAttribute().getId() != null) {
            entity.setAttributeEntity(attributeJpaRepository.findById(attributeValue.getAttribute().getId()).orElse(null));
        }

        return entity;
    }
}

