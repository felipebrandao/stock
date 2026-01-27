package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceHistoryResponse {

    private UUID id;
    private String accessKey;
    private String documentNumber;
    private OffsetDateTime importedAt;
    private OffsetDateTime processedAt;
    private String status;
    private String errorMessage;
    private Integer itemCount;
    private String itemsSummary;
    private BigDecimal totalValue;
}
