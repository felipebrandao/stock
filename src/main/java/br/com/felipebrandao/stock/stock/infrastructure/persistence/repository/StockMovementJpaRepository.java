package br.com.felipebrandao.stock.stock.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockMovementEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockMovementJpaRepository extends JpaRepository<StockMovementEntity, UUID> {

    Page<StockMovementEntity> findAllByProduct_Id(UUID productId, Pageable pageable);

    Page<StockMovementEntity> findAllByFromLocation_IdOrToLocation_Id(UUID fromLocationId, UUID toLocationId, Pageable pageable);
}
