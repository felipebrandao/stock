package br.com.felipebrandao.stock.unit.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.unit.infrastructure.persistence.entity.UnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UnitJpaRepository extends JpaRepository<UnitEntity, UUID> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsByAbbreviationIgnoreCase(String abbreviation);
}

