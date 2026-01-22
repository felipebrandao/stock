package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
public class NfceResponse {

    private UUID id;
    private String qrCodeUrl;
    private String accessKey;
    private String storeName;
    private String status;
    private LocalDate purchaseDate;
    private BigDecimal totalValue;
    private List<NfceItemResponse> items;

}
