package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceItemStatus;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportItemRepository;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeItemEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository.NfceScrapeJpaRepository;
import br.com.felipebrandao.stock.product.domain.repository.ProductAliasRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.shared.utils.TextNormalizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BuildNfceImportReviewUseCase {

    private final NfceImportRepository nfceImportRepository;
    private final NfceImportItemRepository itemRepository;
    private final NfceScrapeJpaRepository scrapeJpaRepository;
    private final ProductAliasRepository productAliasRepository;

    /**
     * Creates reviewable items for a COMPLETED import, if they don't exist yet.
     */
    @Transactional
    public void execute(UUID nfceImportId) {
        NfceImport nfceImport = nfceImportRepository.findById(nfceImportId)
                .orElseThrow(() -> new BusinessException("Importação NFC-e não encontrada"));

        if (nfceImport.getStatus() != NfceStatus.COMPLETED) {
            throw new BusinessException("Importação NFC-e não está COMPLETED");
        }

        // idempotent: if already has items, do nothing
        if (!itemRepository.findByNfceImportId(nfceImportId).isEmpty()) {
            return;
        }

        var scrapeOpt = scrapeJpaRepository.findByAccessKey(nfceImport.getAccessKey());
        if (scrapeOpt.isEmpty()) {
            throw new BusinessException("Dados de scraping não encontrados para a NFC-e");
        }

        List<NfceImportItemEntityLike> scrapedItems = scrapeOpt.get().getItens().stream()
                .map(NfceImportItemEntityLike::from)
                .toList();

        List<NfceImportItem> items = scrapedItems.stream()
                .map(si -> {
                    String normalized = TextNormalizer.normalize(si.description());

                    UUID productId = productAliasRepository.findByEan(si.ean())
                            .map(a -> a.getProductId())
                            .orElseGet(() -> productAliasRepository.findByAliasNormalized(normalized)
                                    .map(a -> a.getProductId())
                                    .orElse(null));

                    NfceItemStatus status = (productId == null) ? NfceItemStatus.NOT_MAPPED : NfceItemStatus.MAPPED;

                    return NfceImportItem.create(
                            nfceImportId,
                            si.itemNumber(),
                            si.description(),
                            normalized,
                            si.ean(),
                            si.unit(),
                            si.quantity(),
                            si.unitPrice(),
                            si.totalPrice(),
                            productId,
                            status,
                            null,
                            null
                    );
                })
                .toList();

        itemRepository.saveAll(items);
    }

    /**
     * Tiny adapter so we don't expose persistence classes in mapping logic.
     */
    private record NfceImportItemEntityLike(
            Integer itemNumber,
            String description,
            java.math.BigDecimal quantity,
            String unit,
            java.math.BigDecimal unitPrice,
            java.math.BigDecimal totalPrice,
            String ean
    ) {
        static NfceImportItemEntityLike from(NfceScrapeItemEntity e) {
            return new NfceImportItemEntityLike(
                    e.getNumero(),
                    e.getDescricao(),
                    e.getQuantidade(),
                    e.getUnidade(),
                    e.getValorUnitario(),
                    e.getValor(),
                    e.getEan()
            );
        }
    }
}
