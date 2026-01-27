package br.com.felipebrandao.stock.api.controller.nfce.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceHistoryPageResponse {

    private List<NfceHistoryResponse> items;
    private long totalItems;
    private int totalPages;
    private int currentPage;
    private int pageSize;

    // Statistics
    private long totalImported;
    private long totalProcessed;
    private long totalPending;
    private long totalErrors;
}
