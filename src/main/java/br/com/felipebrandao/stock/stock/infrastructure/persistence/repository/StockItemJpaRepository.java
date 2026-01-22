package br.com.felipebrandao.stock.stock.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockItemEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StockItemJpaRepository extends JpaRepository<StockItemEntity, UUID> {

    Page<StockItemEntity> findAllByProduct_Id(UUID productId, Pageable pageable);

    Page<StockItemEntity> findAllByLocation_Id(UUID locationId, Pageable pageable);

    Page<StockItemEntity> findAllByProduct_IdAndLocation_Id(UUID productId, UUID locationId, Pageable pageable);

    Page<StockItemEntity> findAllByState(StockItemState state, Pageable pageable);

    Page<StockItemEntity> findAllByProduct_IdAndState(UUID productId, StockItemState state, Pageable pageable);

    Page<StockItemEntity> findAllByLocation_IdAndState(UUID locationId, StockItemState state, Pageable pageable);

    Page<StockItemEntity> findAllByProduct_IdAndLocation_IdAndState(UUID productId, UUID locationId, StockItemState state, Pageable pageable);

    Optional<StockItemEntity> findFirstByProduct_IdAndLocation_IdAndStateOrderByCreatedAtDesc(
            UUID productId,
            UUID locationId,
            StockItemState state
    );
}
