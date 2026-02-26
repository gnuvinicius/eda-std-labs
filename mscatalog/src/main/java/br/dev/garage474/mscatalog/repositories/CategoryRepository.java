package br.dev.garage474.mscatalog.repositories;

import br.dev.garage474.mscatalog.models.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findById(UUID id);

    Page<Category> findAll(Pageable pageable);
}
