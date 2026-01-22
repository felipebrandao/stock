package br.com.felipebrandao.stock.stock.domain.repository;

import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface StockItemRepository {

    StockItem save(StockItem item);

    Optional<StockItem> findById(UUID id);

    Page<StockItem> findAllByFilter(UUID productId, UUID locationId, StockItemState state, Pageable pageable);
}
