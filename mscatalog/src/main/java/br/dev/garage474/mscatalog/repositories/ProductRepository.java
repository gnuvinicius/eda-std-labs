package br.dev.garage474.mscatalog.repositories;

import br.dev.garage474.mscatalog.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    Page<Product> findAll(Pageable pageable);

    List<Product> findAllByCategoryId(UUID categoryId);

    List<Product> findAllByBrandId(UUID brandId);

    boolean existsById(UUID id);
}
