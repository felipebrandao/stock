package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import br.com.felipebrandao.stock.stock.domain.repository.StockItemRepository;
import br.com.felipebrandao.stock.stock.domain.repository.StockMovementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumeStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository movementRepository;

    @Transactional
    public void execute(UUID stockItemId, BigDecimal quantityToConsume, String note) {

        if (stockItemId == null) {
            throw new BusinessException("Id do item de estoque é obrigatório");
        }
        if (quantityToConsume == null || quantityToConsume.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("quantityToConsume deve ser > 0");
        }

        StockItem item = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado"));

        if (item.getState() == StockItemState.CONVERTED) {
            throw new BusinessException("Não é possível consumir um item de estoque CONVERTED");
        }
        if (item.getState() == StockItemState.WRITTEN_OFF) {
            throw new BusinessException("Não é possível consumir um item de estoque WRITTEN_OFF");
        }
        if (item.getQuantity().compareTo(quantityToConsume) < 0) {
            throw new BusinessException("Quantidade insuficiente para consumo");
        }

        item.setQuantity(item.getQuantity().subtract(quantityToConsume));
        item.setUpdatedAt(Instant.now());

        if (item.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            item.setState(StockItemState.WRITTEN_OFF);
        }

        stockItemRepository.save(item);

        StockMovement movement = StockMovement.create(
                StockMovementType.OUT,
                item.getProductId(),
                quantityToConsume,
                item.getLocationId(),
                null,
                note,
                Instant.now()
        );
        movementRepository.save(movement);
    }
}
