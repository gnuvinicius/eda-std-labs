package br.dev.garage474.mscatalog.services.impl;

import br.dev.garage474.mscatalog.dto.ProductCreateDto;
import br.dev.garage474.mscatalog.dto.ProductDto;
import br.dev.garage474.mscatalog.models.Brand;
import br.dev.garage474.mscatalog.models.Category;
import br.dev.garage474.mscatalog.models.Product;
import br.dev.garage474.mscatalog.repositories.BrandRepository;
import br.dev.garage474.mscatalog.repositories.CategoryRepository;
import br.dev.garage474.mscatalog.repositories.ProductRepository;
import br.dev.garage474.mscatalog.services.ProductService;
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
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ProductDto create(UUID tenantId, ProductCreateDto dto) {
        try {
            Product p = new Product();
            p.setTenantId(tenantId);
            p.setName(dto.getName());
            p.setDescription(dto.getDescription());
            p.setSlug(dto.getSlug());

            Brand brand = brandRepository.findByIdAndTenantId(dto.getBrandId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            Category category = categoryRepository.findByIdAndTenantId(dto.getCategoryId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

            p.setBrand(brand);
            p.setCategory(category);

            Product saved = productRepository.save(p);
            return ProductDto.toDto(saved);
        } catch (Exception e) {
            log.error("error creating product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ProductDto getById(UUID tenantId, UUID id) {
        Product p = productRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return ProductDto.toDto(p);
    }

    @Override
    public Page<ProductDto> list(UUID tenantId, Pageable pageable) {
        Page<Product> page = productRepository.findAllByTenantId(tenantId, pageable);
        List<ProductDto> dtos = page.getContent().stream().map(ProductDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public ProductDto update(UUID tenantId, UUID id, ProductCreateDto dto) {
        try {
            Product p = productRepository.findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            p.setName(dto.getName());
            p.setDescription(dto.getDescription());
            p.setSlug(dto.getSlug());

            Brand brand = brandRepository.findByIdAndTenantId(dto.getBrandId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            Category category = categoryRepository.findByIdAndTenantId(dto.getCategoryId(), tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));

            p.setBrand(brand);
            p.setCategory(category);

            Product saved = productRepository.save(p);
            return ProductDto.toDto(saved);
        } catch (Exception e) {
            log.error("error updating product: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(UUID tenantId, UUID id) {
        try {
            Product p = productRepository.findByIdAndTenantId(id, tenantId)
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            productRepository.delete(p);
        } catch (Exception e) {
            log.error("error deleting product: {}", e.getMessage(), e);
            throw e;
        }
    }
}
