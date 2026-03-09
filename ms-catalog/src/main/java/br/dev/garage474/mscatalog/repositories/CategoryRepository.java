package br.dev.garage474.mscatalog.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.dev.garage474.mscatalog.models.Category;

public interface CategoryRepository extends JpaRepository<Category, UUID> {

    Optional<Category> findById(UUID id);
}
