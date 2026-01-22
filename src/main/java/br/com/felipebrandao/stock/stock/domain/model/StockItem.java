package br.com.felipebrandao.stock.stock.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class StockItem {

    private UUID id;
    private UUID productId;
    private UUID locationId;
    private BigDecimal quantity;
    private StockItemState state;
    private LocalDate expiryDate;
    private Instant createdAt;
    private Instant updatedAt;

    public static StockItem create(UUID productId, UUID locationId, BigDecimal quantity) {
        return create(productId, locationId, quantity, StockItemState.CLOSED, null);
    }

    public static StockItem create(UUID productId, UUID locationId, BigDecimal quantity, StockItemState state, LocalDate expiryDate) {
        if (productId == null) {
            throw new BusinessException("Produto é obrigatório");
        }
        if (locationId == null) {
            throw new BusinessException("Local é obrigatório");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Quantidade deve ser >= 0");
        }
        if (state == null) {
            throw new BusinessException("Estado é obrigatório");
        }

        Instant now = Instant.now();
        return new StockItem(
                null,
                productId,
                locationId,
                quantity,
                state,
                expiryDate,
                now,
                now
        );
    }
}
