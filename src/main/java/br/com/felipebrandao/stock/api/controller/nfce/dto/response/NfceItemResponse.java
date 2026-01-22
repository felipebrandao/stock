package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class NfceItemResponse {

    private UUID id;
    private String description;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal unitPrice;
    private UUID productId;
}
