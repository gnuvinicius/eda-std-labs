package br.dev.garage474.mscatalog.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.dev.garage474.mscatalog.dto.ProductDto;
import br.dev.garage474.mscatalog.dto.ShowcaseDto;
import br.dev.garage474.mscatalog.dto.ShowcaseFilterDto;
import br.dev.garage474.mscatalog.models.Product;
import br.dev.garage474.mscatalog.repositories.ProductRepository;
import br.dev.garage474.mscatalog.services.ShowcaseService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ShowcaseServiceImpl implements ShowcaseService {

    private final ProductRepository productRepository;

    public ShowcaseServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Page<ShowcaseDto> list(UUID tenantId, ShowcaseFilterDto filter, Pageable pageable) {
        // For simplicity create a single showcase per page consisting of filtered products.
        List<Product> products;
        if (filter.getBrandId() != null) {
            products = productRepository.findAllByTenantIdAndBrandId(tenantId, filter.getBrandId());
        } else if (filter.getCategoryId() != null) {
            products = productRepository.findAllByTenantIdAndCategoryId(tenantId, filter.getCategoryId());
        } else {
            Page<Product> page = productRepository.findAllByTenantId(tenantId, pageable);
            List<ProductDto> pdtos = page.getContent().stream().map(ProductDto::toDto).collect(Collectors.toList());
            ShowcaseDto showcase = ShowcaseDto.create(UUID.randomUUID(), "Default", "Default showcase", pdtos);
            return new PageImpl<>(List.of(showcase), pageable, page.getTotalElements());
        }

        List<ProductDto> pdtos = products.stream().map(ProductDto::toDto).collect(Collectors.toList());
        ShowcaseDto s = ShowcaseDto.create(UUID.randomUUID(), "Filtered", "Filtered showcase", pdtos);
        return new PageImpl<>(List.of(s), pageable, pdtos.size());
    }

    @Override
    public ShowcaseDto getById(UUID tenantId, UUID id) {
        // Not backed by persistent Showcase entity in this simple implementation
        throw new EntityNotFoundException("Showcase not implemented");
    }
}
