package br.com.felipebrandao.stock.api.controller.stock.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class TransferStockItemRequest {

    @NotNull
    private UUID targetLocationId;

    private String note;
}
