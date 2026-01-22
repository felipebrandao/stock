package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceItemStatus;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportItemRepository;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.stock.application.usecase.CreateStockMovementUseCase;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApproveNfceImportUseCase {

    private final NfceImportRepository nfceImportRepository;
    private final NfceImportItemRepository itemRepository;
    private final CreateStockMovementUseCase createStockMovementUseCase;

    @Transactional
    public void execute(UUID nfceImportId) {
        NfceImport nfceImport = nfceImportRepository.findById(nfceImportId)
                .orElseThrow(() -> new BusinessException("Importação NFC-e não encontrada"));

        if (nfceImport.getStatus() == NfceStatus.APPLIED) {
            throw new BusinessException("Importação NFC-e já está APPLIED");
        }
        if (nfceImport.getStatus() != NfceStatus.COMPLETED) {
            throw new BusinessException("Importação NFC-e ainda não está COMPLETED");
        }

        List<NfceImportItem> items = itemRepository.findByNfceImportId(nfceImportId);
        if (items.isEmpty()) {
            throw new BusinessException("Importação NFC-e não possui itens para aprovação");
        }

        // validate all items mapped and reviewed
        for (NfceImportItem item : items) {
            if (item.getStatus() != NfceItemStatus.MAPPED || item.getProductId() == null) {
                throw new BusinessException("Existem itens não mapeados; revise antes de aprovar");
            }
            if (item.getLocationId() == null) {
                throw new BusinessException("Existem itens sem local definido; revise antes de aprovar");
            }
        }

        for (NfceImportItem item : items) {
            String note = "NFC-e import " + nfceImportId + " item " + (item.getItemNumber() == null ? "" : item.getItemNumber());
            createStockMovementUseCase.execute(
                    StockMovementType.IN,
                    item.getProductId(),
                    item.getQuantity(),
                    null,
                    item.getLocationId(),
                    note,
                    Instant.now()
            );
        }

        nfceImport.setStatus(NfceStatus.APPLIED);
        nfceImportRepository.save(nfceImport);

        log.info("[nfce] Importação {} aprovada e aplicada no estoque", nfceImportId);
    }
}
