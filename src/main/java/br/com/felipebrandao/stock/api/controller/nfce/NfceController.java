package br.com.felipebrandao.stock.api.controller.nfce;

import br.com.felipebrandao.stock.api.controller.nfce.dto.request.ImportNfceRequest;
import br.com.felipebrandao.stock.api.controller.nfce.dto.request.UpdateNfceImportReviewRequest;
import br.com.felipebrandao.stock.api.controller.nfce.dto.response.NfceHistoryPageResponse;
import br.com.felipebrandao.stock.api.controller.nfce.dto.response.NfceHistoryResponse;
import br.com.felipebrandao.stock.api.controller.nfce.dto.response.NfceImportReviewItemResponse;
import br.com.felipebrandao.stock.api.controller.nfce.dto.response.NfceImportReviewResponse;
import br.com.felipebrandao.stock.api.controller.nfce.dto.response.NfceResponse;
import br.com.felipebrandao.stock.nfce.application.usecase.ApproveNfceImportUseCase;
import br.com.felipebrandao.stock.nfce.application.usecase.GetNfceHistoryUseCase;
import br.com.felipebrandao.stock.nfce.application.usecase.GetNfceImportReviewUseCase;
import br.com.felipebrandao.stock.nfce.application.usecase.ImportNfceUseCase;
import br.com.felipebrandao.stock.nfce.application.usecase.RetryNfceImportUseCase;
import br.com.felipebrandao.stock.nfce.application.usecase.UpdateNfceImportReviewUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/nfce")
@RequiredArgsConstructor
public class NfceController {

    private final ImportNfceUseCase importNfceUseCase;
    private final GetNfceImportReviewUseCase getNfceImportReviewUseCase;
    private final UpdateNfceImportReviewUseCase updateNfceImportReviewUseCase;
    private final ApproveNfceImportUseCase approveNfceImportUseCase;
    private final GetNfceHistoryUseCase getNfceHistoryUseCase;
    private final RetryNfceImportUseCase retryNfceImportUseCase;

    @PostMapping("/import")
    public ResponseEntity<NfceResponse> importNfce(
            @RequestBody ImportNfceRequest request
    ) {
        UUID id = importNfceUseCase.execute(
                request.getQrCodeUrl()
        );
        return ResponseEntity.accepted()
                .body(NfceResponse.builder()
                        .id(id)
                        .build());
    }

    @GetMapping("/imports/{id}/review")
    public ResponseEntity<NfceImportReviewResponse> getImportReview(@PathVariable("id") UUID id) {
        var result = getNfceImportReviewUseCase.execute(id);

        return ResponseEntity.ok(
                NfceImportReviewResponse.builder()
                        .id(result.nfceImport().getId())
                        .status(result.nfceImport().getStatus().name())
                        .items(result.items().stream()
                                .map(i -> NfceImportReviewItemResponse.builder()
                                        .id(i.getId())
                                        .itemNumber(i.getItemNumber())
                                        .description(i.getDescription())
                                        .ean(i.getEan())
                                        .unit(i.getUnit())
                                        .quantity(i.getQuantity())
                                        .unitPrice(i.getUnitPrice())
                                        .totalPrice(i.getTotalPrice())
                                        .productId(i.getProductId())
                                        .status(i.getStatus().name())
                                        .expiryDate(i.getExpiryDate())
                                        .locationId(i.getLocationId())
                                        .build())
                                .toList())
                        .build()
        );
    }

    @PutMapping("/imports/{id}/review")
    public ResponseEntity<Void> updateImportReview(
            @PathVariable("id") UUID id,
            @RequestBody UpdateNfceImportReviewRequest request
    ) {
        List<UpdateNfceImportReviewUseCase.UpdateItemCommand> updates =
                request.getItems() == null ? List.of() :
                        request.getItems().stream()
                                .map(i -> new UpdateNfceImportReviewUseCase.UpdateItemCommand(
                                        i.getId(),
                                        i.getProductId(),
                                        i.getQuantity(),
                                        i.getExpiryDate(),
                                        i.getLocationId(),
                                        i.getSaveMapping()
                                ))
                                .toList();

        updateNfceImportReviewUseCase.execute(id, updates);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/imports/{id}/approve")
    public ResponseEntity<Void> approveImport(@PathVariable("id") UUID id) {
        approveNfceImportUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/imports/{id}/retry")
    public ResponseEntity<Void> retryImport(@PathVariable("id") UUID id) {
        retryNfceImportUseCase.execute(id);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/history")
    public ResponseEntity<NfceHistoryPageResponse> getHistory(
            @RequestParam(value = "status", required = false, defaultValue = "ALL") String status,
            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        var result = getNfceHistoryUseCase.execute(status, page, size);

        var items = result.items().stream()
                .map(item -> NfceHistoryResponse.builder()
                        .id(item.id())
                        .accessKey(item.accessKey())
                        .documentNumber(item.documentNumber())
                        .importedAt(item.importedAt())
                        .processedAt(item.processedAt())
                        .status(item.status())
                        .errorMessage(item.errorMessage())
                        .itemCount(item.itemCount())
                        .itemsSummary(item.itemsSummary())
                        .totalValue(item.totalValue())
                        .build())
                .toList();

        int totalPages = (int) Math.ceil((double) result.totalItems() / result.pageSize());

        return ResponseEntity.ok(
                NfceHistoryPageResponse.builder()
                        .items(items)
                        .totalItems(result.totalItems())
                        .totalPages(totalPages)
                        .currentPage(result.currentPage())
                        .pageSize(result.pageSize())
                        .totalImported(result.totalImported())
                        .totalProcessed(result.totalProcessed())
                        .totalPending(result.totalPending())
                        .totalErrors(result.totalErrors())
                        .build()
        );
    }

}
