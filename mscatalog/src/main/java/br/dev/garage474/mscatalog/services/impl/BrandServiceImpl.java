package br.dev.garage474.mscatalog.services.impl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.dev.garage474.mscatalog.dto.BrandCreateDto;
import br.dev.garage474.mscatalog.dto.BrandDto;
import br.dev.garage474.mscatalog.models.Brand;
import br.dev.garage474.mscatalog.repositories.BrandRepository;
import br.dev.garage474.mscatalog.services.BrandService;
import jakarta.persistence.EntityNotFoundException;

@Service
public class BrandServiceImpl implements BrandService {

    private static final Logger log = LoggerFactory.getLogger(BrandServiceImpl.class);

    private final BrandRepository brandRepository;

    public BrandServiceImpl(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @Override
    @Transactional
    public BrandDto create(BrandCreateDto dto) {
        try {
            Brand b = new Brand();
            b.setName(dto.getName());
            Brand saved = brandRepository.save(b);
            return BrandDto.toDto(saved);
        } catch (Exception e) {
            log.error("error creating brand: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public BrandDto getById(UUID id) {
        Brand b = brandRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
        return BrandDto.toDto(b);
    }

    @Override
    public Page<BrandDto> list(Pageable pageable) {
        Page<Brand> page = brandRepository.findAll(pageable);
        List<BrandDto> dtos = page.getContent().stream().map(BrandDto::toDto).collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, page.getTotalElements());
    }

    @Override
    @Transactional
    public BrandDto update(UUID id, BrandCreateDto dto) {
        try {
            Brand b = brandRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            b.setName(dto.getName());
            Brand saved = brandRepository.save(b);
            return BrandDto.toDto(saved);
        } catch (Exception e) {
            log.error("error updating brand: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        try {
            Brand b = brandRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Brand not found"));
            brandRepository.delete(b);
        } catch (Exception e) {
            log.error("error deleting brand: {}", e.getMessage(), e);
            throw e;
        }
    }
}
