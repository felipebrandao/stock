package br.com.felipebrandao.stock.api.controller.stock.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsumeStockItemRequest {

    @NotNull
    @Positive
    private BigDecimal quantity;

    private String note;
}
