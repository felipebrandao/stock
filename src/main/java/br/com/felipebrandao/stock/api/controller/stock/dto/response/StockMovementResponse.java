package br.com.felipebrandao.stock.api.controller.stock.dto.response;

import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class StockMovementResponse {

    private UUID id;
    private StockMovementType type;
    private UUID productId;
    private BigDecimal quantity;
    private UUID fromLocationId;
    private UUID toLocationId;
    private String note;
    private Instant occurredAt;
    private Instant createdAt;
}
