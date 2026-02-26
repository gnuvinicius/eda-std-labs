package br.dev.garage474.mscatalog.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.garage474.mscatalog.models.Product;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findById(UUID id);

    List<Product> findAllByCategoryId(UUID categoryId);

    List<Product> findAllByBrandId(UUID brandId);

    boolean existsById(UUID id);
}
