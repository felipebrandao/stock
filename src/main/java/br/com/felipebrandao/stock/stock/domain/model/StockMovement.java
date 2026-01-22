package br.com.felipebrandao.stock.stock.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StockMovement {

    private UUID id;
    private StockMovementType type;
    private UUID productId;
    private BigDecimal quantity;
    private UUID fromLocationId;
    private UUID toLocationId;
    private String note;
    private Instant occurredAt;
    private Instant createdAt;

    public static StockMovement create(
            StockMovementType type,
            UUID productId,
            BigDecimal quantity,
            UUID fromLocationId,
            UUID toLocationId,
            String note,
            Instant occurredAt
    ) {
        if (type == null) {
            throw new BusinessException("Tipo de movimentação é obrigatório");
        }
        if (productId == null) {
            throw new BusinessException("Produto é obrigatório");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Quantidade deve ser > 0");
        }

        // Type-specific constraints
        if (type == StockMovementType.IN) {
            if (toLocationId == null) {
                throw new BusinessException("toLocationId é obrigatório para movimentações IN");
            }
        }
        if (type == StockMovementType.OUT) {
            if (fromLocationId == null) {
                throw new BusinessException("fromLocationId é obrigatório para movimentações OUT");
            }
        }
        if (type == StockMovementType.TRANSFER) {
            if (fromLocationId == null || toLocationId == null) {
                throw new BusinessException("fromLocationId e toLocationId são obrigatórios para movimentações TRANSFER");
            }
            if (fromLocationId.equals(toLocationId)) {
                throw new BusinessException("fromLocationId e toLocationId devem ser diferentes");
            }
        }

        Instant now = Instant.now();
        return new StockMovement(
                null,
                type,
                productId,
                quantity,
                fromLocationId,
                toLocationId,
                note == null ? null : note.trim(),
                occurredAt == null ? now : occurredAt,
                now
        );
    }
}
