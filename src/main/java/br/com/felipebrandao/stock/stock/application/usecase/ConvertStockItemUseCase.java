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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConvertStockItemUseCase {

    private final StockItemRepository stockItemRepository;
    private final StockMovementRepository movementRepository;

    @Transactional
    public List<UUID> execute(UUID sourceStockItemId, List<BigDecimal> destinationQuantities, String note) {

        if (sourceStockItemId == null) {
            throw new BusinessException("Id do item de estoque de origem é obrigatório");
        }
        if (destinationQuantities == null || destinationQuantities.isEmpty()) {
            throw new BusinessException("destinationQuantities é obrigatório");
        }
        if (destinationQuantities.stream().anyMatch(q -> q == null || q.compareTo(BigDecimal.ZERO) <= 0)) {
            throw new BusinessException("Todas as quantidades de destino devem ser > 0");
        }

        StockItem source = stockItemRepository.findById(sourceStockItemId)
                .orElseThrow(() -> new NotFoundException("Item de estoque não encontrado"));

        if (source.getState() == StockItemState.CONVERTED) {
            throw new BusinessException("Item de estoque já está CONVERTED");
        }
        if (source.getState() == StockItemState.WRITTEN_OFF) {
            throw new BusinessException("Não é possível converter um item de estoque WRITTEN_OFF");
        }

        BigDecimal sum = destinationQuantities.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        if (source.getQuantity().compareTo(sum) < 0) {
            throw new BusinessException("Quantidade insuficiente para conversão");
        }

        // mark source as converted (and reduce quantity if partially converted)
        source.setQuantity(source.getQuantity().subtract(sum));
        if (source.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            source.setState(StockItemState.CONVERTED);
        }
        source.setUpdatedAt(Instant.now());
        stockItemRepository.save(source);

        List<UUID> createdIds = new ArrayList<>();
        for (BigDecimal q : destinationQuantities) {
            StockItem created = StockItem.create(
                    source.getProductId(),
                    source.getLocationId(),
                    q,
                    StockItemState.IN_USE,
                    source.getExpiryDate()
            );
            created.setCreatedAt(Instant.now());
            created.setUpdatedAt(Instant.now());
            created = stockItemRepository.save(created);
            createdIds.add(created.getId());

            StockMovement movement = StockMovement.create(
                    StockMovementType.CONVERSION,
                    source.getProductId(),
                    q,
                    source.getLocationId(),
                    source.getLocationId(),
                    note,
                    Instant.now()
            );
            movementRepository.save(movement);
        }

        return createdIds;
    }
}
