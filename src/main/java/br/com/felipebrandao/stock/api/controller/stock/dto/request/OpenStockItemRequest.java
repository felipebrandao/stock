package br.com.felipebrandao.stock.api.controller.stock.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OpenStockItemRequest {

    @NotNull
    @Positive
    private BigDecimal quantity;

    @NotNull
    private UUID targetLocationId;

    private String note;
}
