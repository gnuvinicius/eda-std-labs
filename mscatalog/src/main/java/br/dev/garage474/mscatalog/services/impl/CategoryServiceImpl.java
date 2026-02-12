package br.dev.garage474.mscatalog.services.impl;

import br.dev.garage474.mscatalog.dto.CategoryCreateDto;
import br.dev.garage474.mscatalog.dto.CategoryDto;
import br.dev.garage474.mscatalog.models.Category;
import br.dev.garage474.mscatalog.repositories.CategoryRepository;
import br.dev.garage474.mscatalog.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger log = LoggerFactory.getLogger(CategoryServiceImpl.class);

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public CategoryDto create(UUID tenantId, CategoryCreateDto dto) {
        try {
            Category c = new Category();
            c.setTenantId(tenantId);
            c.setName(dto.getName());
            if (dto.getParentId() != null) {
                Category parent = categoryRepository.findByIdAndTenantId(dto.getParentId(), tenantId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
                c.setParent(parent);
            }
            Category saved = categoryRepository.save(c);
            return CategoryDto.toDto(saved);
        } catch (Exception e) {
            log.error("error creating category: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public CategoryDto getById(UUID tenantId, UUID id) {
        Category c = categoryRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        return CategoryDto.toDto(c);
    }

    @Override
    public Page<CategoryDto> list(UUID tenantId, Pageable pageable) {
        Page<Category> page = categoryRepository.findAllByTenantId(tenantId, pageable);
        List<CategoryDto> dtos = page.getContent().stream().map(CategoryDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public CategoryDto update(UUID tenantId, UUID id, CategoryCreateDto dto) {
        try {
            Category c = categoryRepository.findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            c.setName(dto.getName());
            if (dto.getParentId() != null) {
                Category parent = categoryRepository.findByIdAndTenantId(dto.getParentId(), tenantId)
                        .orElseThrow(() -> new EntityNotFoundException("Parent category not found"));
                c.setParent(parent);
            } else {
                c.setParent(null);
            }
            Category saved = categoryRepository.save(c);
            return CategoryDto.toDto(saved);
        } catch (Exception e) {
            log.error("error updating category: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(UUID tenantId, UUID id) {
        try {
            Category c = categoryRepository.findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            categoryRepository.delete(c);
        } catch (Exception e) {
            log.error("error deleting category: {}", e.getMessage(), e);
            throw e;
        }
    }
}
