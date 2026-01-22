package br.com.felipebrandao.stock.api.controller.stock.dto.response;

import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StockItemResponse {

    private UUID id;
    private UUID productId;
    private UUID locationId;
    private BigDecimal quantity;
    private StockItemState state;
    private LocalDate expiryDate;
    private Instant createdAt;
    private Instant updatedAt;
}
