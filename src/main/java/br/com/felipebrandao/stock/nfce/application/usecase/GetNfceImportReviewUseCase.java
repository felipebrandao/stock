package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportItemRepository;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetNfceImportReviewUseCase {

    private final NfceImportRepository nfceImportRepository;
    private final NfceImportItemRepository itemRepository;
    private final BuildNfceImportReviewUseCase buildNfceImportReviewUseCase;

    public Result execute(UUID nfceImportId) {
        // ensure it exists
        NfceImport nfceImport = nfceImportRepository.findById(nfceImportId)
                .orElseThrow(() -> new BusinessException("Importação NFC-e não encontrada"));

        // build items on demand after scraping completed
        if (nfceImport.getStatus() == br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus.COMPLETED) {
            buildNfceImportReviewUseCase.execute(nfceImportId);
        }

        List<NfceImportItem> items = itemRepository.findByNfceImportId(nfceImportId);

        return new Result(nfceImport, items);
    }

    public record Result(NfceImport nfceImport, List<NfceImportItem> items) {
    }
}
