package br.com.felipebrandao.stock.category.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.category.infrastructure.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
}
