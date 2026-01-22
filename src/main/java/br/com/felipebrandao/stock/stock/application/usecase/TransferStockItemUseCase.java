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
public class TransferStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository movementRepository;
    private final LocationRepository locationRepository;

    @Transactional
    public void execute(UUID stockItemId, UUID targetLocationId, String note) {

        if (stockItemId == null) {
            throw new BusinessException("Id do item de estoque é obrigatório");
        }
        if (targetLocationId == null) {
            throw new BusinessException("Id do local de destino é obrigatório");
        }
        if (locationRepository.findById(targetLocationId).isEmpty()) {
            throw new BusinessException("Local de destino não encontrado");
        }

        StockItem item = stockItemRepository.findById(stockItemId)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado"));

        if (item.getState() == StockItemState.WRITTEN_OFF) {
            throw new BusinessException("Não é possível transferir um item de estoque WRITTEN_OFF");
        }
        if (item.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Não é possível transferir um item de estoque sem quantidade");
        }
        if (item.getLocationId().equals(targetLocationId)) {
            throw new BusinessException("O local de destino deve ser diferente do local de origem");
        }

        UUID fromLocationId = item.getLocationId();
        item.setLocationId(targetLocationId);
        item.setUpdatedAt(Instant.now());
        stockItemRepository.save(item);

        StockMovement movement = StockMovement.create(
                StockMovementType.TRANSFER,
                item.getProductId(),
                item.getQuantity(),
                fromLocationId,
                targetLocationId,
                note,
                Instant.now()
        );
        movementRepository.save(movement);
    }
}
