package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
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
public class OpenStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository movementRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public UUID execute(UUID stockItemId, BigDecimal quantityToOpen, UUID targetLocationId, String note) {

        if (stockItemId == null) {
            throw new BusinessException("Id do item de estoque é obrigatório");
        }
        if (quantityToOpen == null || quantityToOpen.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("quantityToOpen deve ser > 0");
        }
        if (targetLocationId == null) {
            throw new BusinessException("Id do local de destino é obrigatório");
        }
        if (locationRepository.findById(targetLocationId).isEmpty()) {
            throw new BusinessException("Local de destino não encontrado");
        }

        StockItem source = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado"));

        if (source.getState() != StockItemState.CLOSED) {
            throw new BusinessException("Apenas itens de estoque CLOSED podem ser abertos");
        }
        if (source.getQuantity().compareTo(quantityToOpen) < 0) {
            throw new BusinessException("Quantidade insuficiente para abertura");
        }

        // reduce source
        source.setQuantity(source.getQuantity().subtract(quantityToOpen));
        source.setUpdatedAt(Instant.now());
        stockItemRepository.save(source);

        // create opened item (new lot)
        StockItem opened = StockItem.create(
                source.getProductId(),
                targetLocationId,
                quantityToOpen,
                StockItemState.IN_USE,
                source.getExpiryDate()
        );
        opened.setCreatedAt(Instant.now());
        opened.setUpdatedAt(Instant.now());
        opened = stockItemRepository.save(opened);

        // movement record
        StockMovement movement = StockMovement.create(
                StockMovementType.STATE_CHANGE,
                source.getProductId(),
                quantityToOpen,
                source.getLocationId(),
                targetLocationId,
                note,
                Instant.now()
        );
        movementRepository.save(movement);

        return opened.getId();
    }
}
