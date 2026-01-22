package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class NfceImportReviewItemResponse {

    private UUID id;
    private Integer itemNumber;

    private String description;
    private String ean;
    private String unit;

    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private UUID productId;
    private String status;

    private LocalDate expiryDate;
    private UUID locationId;
}
