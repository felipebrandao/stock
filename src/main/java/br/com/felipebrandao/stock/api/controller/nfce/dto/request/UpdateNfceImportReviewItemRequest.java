package br.com.felipebrandao.stock.api.controller.nfce.dto.request;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class UpdateNfceImportReviewItemRequest {

    private UUID id;
    private UUID productId;
    private BigDecimal quantity;
    private LocalDate expiryDate;
    private UUID locationId;
    private Boolean saveMapping;
}
