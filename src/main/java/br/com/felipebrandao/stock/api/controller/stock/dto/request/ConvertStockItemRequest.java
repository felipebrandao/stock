package br.com.felipebrandao.stock.api.controller.stock.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ConvertStockItemRequest {

    @NotEmpty
    private List<@NotNull @Positive BigDecimal> destinationQuantities;

    private String note;
}
