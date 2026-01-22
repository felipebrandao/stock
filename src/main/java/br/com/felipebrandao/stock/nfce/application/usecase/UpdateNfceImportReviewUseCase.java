package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportItemRepository;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.product.domain.model.ProductAlias;
import br.com.felipebrandao.stock.product.domain.repository.ProductAliasRepository;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.shared.utils.TextNormalizer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateNfceImportReviewUseCase {

    private final NfceImportRepository nfceImportRepository;
    private final NfceImportItemRepository itemRepository;

    private final ProductRepository productRepository;
    private final LocationRepository locationRepository;
    private final ProductAliasRepository productAliasRepository;

    @Transactional
    public void execute(UUID nfceImportId, List<UpdateItemCommand> updates) {
        NfceImport nfceImport = nfceImportRepository.findById(nfceImportId)
                .orElseThrow(() -> new BusinessException("Importação NFC-e não encontrada"));

        if (nfceImport.getStatus() == NfceStatus.APPLIED) {
            throw new BusinessException("Importação NFC-e já foi APPLIED e não pode ser editada");
        }

        if (updates == null || updates.isEmpty()) {
            return;
        }

        for (UpdateItemCommand cmd : updates) {
            NfceImportItem item = itemRepository.findById(cmd.itemId())
                    .orElseThrow(() -> new BusinessException("Item da importação NFC-e não encontrado"));

            if (!nfceImportId.equals(item.getNfceImportId())) {
                throw new BusinessException("Item não pertence à importação informada");
            }

            if (cmd.locationId() != null && locationRepository.findById(cmd.locationId()).isEmpty()) {
                throw new BusinessException("Local não encontrado");
            }
            if (cmd.productId() != null && productRepository.findById(cmd.productId()).isEmpty()) {
                throw new BusinessException("Produto não encontrado");
            }

            // location is mandatory for review
            UUID locationId = cmd.locationId() == null ? item.getLocationId() : cmd.locationId();
            if (locationId == null) {
                throw new BusinessException("Local é obrigatório");
            }

            item.updateReview(cmd.productId(), cmd.quantity(), cmd.expiryDate(), locationId);
            itemRepository.save(item);

            if (Boolean.TRUE.equals(cmd.saveMapping()) && cmd.productId() != null) {
                String alias = TextNormalizer.normalize(item.getDescription());
                String ean = item.getEan();

                // best effort: ignore duplicates (unique constraint)
                if (productAliasRepository.findByAliasNormalized(alias).isEmpty()
                        && (ean == null || productAliasRepository.findByEan(ean).isEmpty())) {
                    try {
                        productAliasRepository.save(ProductAlias.create(alias, ean, cmd.productId()));
                    } catch (Exception ignored) {
                        // concorrência/duplicidade: ok
                    }
                }
            }
        }
    }

    public record UpdateItemCommand(
            UUID itemId,
            UUID productId,
            BigDecimal quantity,
            LocalDate expiryDate,
            UUID locationId,
            Boolean saveMapping
    ) {
    }
}
