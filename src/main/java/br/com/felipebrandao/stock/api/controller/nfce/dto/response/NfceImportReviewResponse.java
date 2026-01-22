package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class NfceImportReviewResponse {

    private UUID id;
    private String status;

    private List<NfceImportReviewItemResponse> items;
}
