package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
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
public class CreateStockMovementUseCase {

    private final StockMovementRepository movementRepository;
    private final StockItemRepository stockItemRepository;
    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;

    /**
     * Compatibility note:
     * - IN: creates a NEW CLOSED StockItem lot in toLocationId.
     * - OUT: requires fromLocationId AND consumes from a specific lot (fromStockItemId).
     * - TRANSFER: requires fromLocationId/toLocationId AND transfers a specific lot (fromStockItemId).
     */
    @Transactional
    public UUID execute(
            StockMovementType type,
            UUID productId,
            BigDecimal quantity,
            UUID fromLocationId,
            UUID toLocationId,
            String note,
            Instant occurredAt
    ) {
        StockMovement movement = StockMovement.create(
                type,
                productId,
                quantity,
                fromLocationId,
                toLocationId,
                note,
                occurredAt
        );

        if (productRepository.findById(productId).isEmpty()) {
            throw new BusinessException("Produto não encontrado");
        }

        if (movement.getFromLocationId() != null && locationRepository.findById(movement.getFromLocationId()).isEmpty()) {
            throw new BusinessException("Local de origem não encontrado");
        }
        if (movement.getToLocationId() != null && locationRepository.findById(movement.getToLocationId()).isEmpty()) {
            throw new BusinessException("Local de destino não encontrado");
        }

        switch (movement.getType()) {
            case IN -> applyIn(movement);
            case OUT -> throw new BusinessException("Use /api/stock-items/{id}/consume para movimentações OUT no nível do item");
            case TRANSFER -> throw new BusinessException("Use /api/stock-items/{id}/transfer para movimentações TRANSFER no nível do item");
            case STATE_CHANGE -> throw new BusinessException("Use /api/stock-items/{id}/open para mudanças de estado");
            case CONVERSION -> throw new BusinessException("Use /api/stock-items/{id}/convert para conversões");
        }

        StockMovement saved = movementRepository.save(movement);
        return saved.getId();
    }

    private void applyIn(StockMovement movement) {
        UUID productId = movement.getProductId();
        UUID locationId = movement.getToLocationId();

        StockItem item = StockItem.create(productId, locationId, movement.getQuantity(), StockItemState.CLOSED, null);
        item.setCreatedAt(Instant.now());
        item.setUpdatedAt(Instant.now());
        stockItemRepository.save(item);
    }
}
