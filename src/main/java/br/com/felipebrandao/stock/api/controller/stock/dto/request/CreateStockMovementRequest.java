package br.com.felipebrandao.stock.api.controller.stock.dto.request;

import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
public class CreateStockMovementRequest {

    @NotNull
    private StockMovementType type;

    @NotNull
    private UUID productId;

    @NotNull
    @Positive
    private BigDecimal quantity;

    private UUID fromLocationId;
    private UUID toLocationId;

    private String note;

    private Instant occurredAt;
}
