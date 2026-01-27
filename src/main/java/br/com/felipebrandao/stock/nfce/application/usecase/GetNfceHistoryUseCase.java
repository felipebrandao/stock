package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository.NfceScrapeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetNfceHistoryUseCase {

    private final NfceImportRepository nfceImportRepository;
    private final NfceScrapeJpaRepository nfceScrapeJpaRepository;

    @Transactional(readOnly = true)
    public HistoryResult execute(String statusFilter, int page, int size) {
        List<NfceImport> imports;

        if (statusFilter != null && !statusFilter.equalsIgnoreCase("ALL")) {
            NfceStatus status = NfceStatus.valueOf(statusFilter.toUpperCase());
            imports = nfceImportRepository.findByStatusPaginated(status, page, size);
        } else {
            imports = nfceImportRepository.findAllPaginated(page, size);
        }

        long totalImported = nfceImportRepository.count();
        long totalProcessed = nfceImportRepository.countByStatus(NfceStatus.COMPLETED)
                + nfceImportRepository.countByStatus(NfceStatus.APPLIED);
        long totalPending = nfceImportRepository.countByStatus(NfceStatus.PENDING)
                + nfceImportRepository.countByStatus(NfceStatus.PROCESSING);
        long totalErrors = nfceImportRepository.countByStatus(NfceStatus.ERROR);

        List<HistoryItem> items = imports.stream()
                .map(this::toHistoryItem)
                .toList();

        long totalCount = statusFilter != null && !statusFilter.equalsIgnoreCase("ALL")
                ? nfceImportRepository.countByStatus(NfceStatus.valueOf(statusFilter.toUpperCase()))
                : totalImported;

        return new HistoryResult(
                items,
                totalCount,
                page,
                size,
                totalImported,
                totalProcessed,
                totalPending,
                totalErrors
        );
    }

    private HistoryItem toHistoryItem(NfceImport nfceImport) {
        Optional<NfceScrapeEntity> scrapeOpt = nfceScrapeJpaRepository.findByAccessKeyWithItems(nfceImport.getAccessKey());

        String documentNumber = null;
        Integer itemCount = null;
        String itemsSummary = null;
        BigDecimal totalValue = null;

        if (scrapeOpt.isPresent()) {
            NfceScrapeEntity scrape = scrapeOpt.get();
            if (scrape.getNumero() != null) {
                int serie = scrape.getSerie() != null ? scrape.getSerie() : 0;
                documentNumber = String.format("%d-%02d", scrape.getNumero(), serie);
            }
            itemCount = scrape.getItens() != null ? scrape.getItens().size() : 0;
            totalValue = scrape.getValorTotalNota();

            if (scrape.getItens() != null && !scrape.getItens().isEmpty()) {
                itemsSummary = scrape.getItens().stream()
                        .limit(3)
                        .map(br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeItemEntity::getDescricao)
                        .collect(Collectors.joining(", "));
                if (scrape.getItens().size() > 3) {
                    itemsSummary += "...";
                }
            }
        }

        return new HistoryItem(
                nfceImport.getId(),
                nfceImport.getAccessKey(),
                documentNumber,
                OffsetDateTime.ofInstant(nfceImport.getCreatedAt(), ZoneId.systemDefault()),
                nfceImport.getUpdatedAt() != null ? OffsetDateTime.ofInstant(nfceImport.getUpdatedAt(), ZoneId.systemDefault()) : null,
                nfceImport.getStatus().name(),
                nfceImport.getErrorMessage(),
                itemCount,
                itemsSummary,
                totalValue
        );
    }

    public record HistoryItem(
            UUID id,
            String accessKey,
            String documentNumber,
            OffsetDateTime importedAt,
            OffsetDateTime processedAt,
            String status,
            String errorMessage,
            Integer itemCount,
            String itemsSummary,
            BigDecimal totalValue
    ) {}

    public record HistoryResult(
            List<HistoryItem> items,
            long totalItems,
            int currentPage,
            int pageSize,
            long totalImported,
            long totalProcessed,
            long totalPending,
            long totalErrors
    ) {}
}
