package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.domain.repository.StockItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetStockItemUseCase {

    private final StockItemRepository repository;

    public StockItem execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado"));
    }
}
