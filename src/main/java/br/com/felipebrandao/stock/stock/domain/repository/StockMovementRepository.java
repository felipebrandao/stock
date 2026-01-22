package br.com.felipebrandao.stock.stock.domain.repository;

import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface StockMovementRepository {

    StockMovement save(StockMovement movement);

    Page<StockMovement> findAll(Pageable pageable);

    Page<StockMovement> findAllByProductId(UUID productId, Pageable pageable);

    Page<StockMovement> findAllByLocationId(UUID locationId, Pageable pageable);
}
