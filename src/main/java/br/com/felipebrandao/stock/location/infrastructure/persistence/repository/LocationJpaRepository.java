package br.com.felipebrandao.stock.location.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface LocationJpaRepository extends JpaRepository<LocationEntity, UUID> {

    boolean existsByNameIgnoreCase(String name);
}
