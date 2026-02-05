package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.application.service.AsyncNfceImportProcessorService;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RetryNfceImportUseCase {

    private final NfceImportRepository repository;
    private final AsyncNfceImportProcessorService asyncProcessor;

    @Value("${nfce.import.max-retry-attempts:3}")
    private int maxRetryAttempts;

    @Transactional
    public void execute(UUID nfceImportId) {
        log.info("[retry-nfce] Iniciando retry para importação id={}", nfceImportId);

        NfceImport nfceImport = repository.findById(nfceImportId)
                .orElseThrow(() -> new BusinessException("Importação NFC-e não encontrada"));

        if (nfceImport.getStatus() != NfceStatus.ERROR) {
            throw new BusinessException(
                    String.format("Importação não pode ser retentada. Status atual: %s. Apenas importações com status ERROR podem ser retentadas.",
                            nfceImport.getStatus()));
        }

        Integer currentAttempts = nfceImport.getAttempts() != null ? nfceImport.getAttempts() : 0;
        if (currentAttempts >= maxRetryAttempts) {
            throw new BusinessException(
                    String.format("Número máximo de tentativas atingido. Tentativas: %d/%d",
                            currentAttempts, maxRetryAttempts));
        }

        nfceImport.setStatus(NfceStatus.PENDING);
        nfceImport.setErrorMessage(null);
        repository.save(nfceImport);

        log.info("[retry-nfce] Status resetado para PENDING. id={} attempts={}", nfceImportId, currentAttempts);

        asyncProcessor.processAsync(nfceImportId);

        log.info("[retry-nfce] Retry disparado com sucesso para id={}", nfceImportId);
    }
}
