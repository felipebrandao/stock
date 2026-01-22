package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.domain.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListStockItemsUseCase {

    private final StockItemRepository repository;

    public Page<StockItem> execute(UUID productId, UUID locationId, StockItemState state, Pageable pageable) {
        return repository.findAllByFilter(productId, locationId, state, pageable);
    }
}
