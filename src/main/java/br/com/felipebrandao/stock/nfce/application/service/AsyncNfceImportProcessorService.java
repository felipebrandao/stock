package br.com.felipebrandao.stock.nfce.application.service;

import br.com.felipebrandao.stock.nfce.application.usecase.ProcessNfceImportUseCase;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsyncNfceImportProcessorService {

    private final NfceImportRepository repository;
    private final ProcessNfceImportUseCase processNfceImportUseCase;
    private final NfceImportStatusUpdater statusUpdater;

    @Async("nfceImportExecutor")
    public void processAsync(UUID nfceImportId) {
        log.info("[async-processor] Iniciando processamento assíncrono para importação id={}", nfceImportId);

        try {
            NfceImport nfceImport = repository.findById(nfceImportId)
                    .orElseThrow(() -> new IllegalStateException("Importação não encontrada: " + nfceImportId));

            statusUpdater.updateToProcessing(nfceImport);

            processNfceImportUseCase.execute(nfceImport);

            log.info("[async-processor] Processamento assíncrono concluído com sucesso para id={}", nfceImportId);
        } catch (Exception ex) {
            log.error("[async-processor] Erro inesperado no processamento assíncrono para id={}", nfceImportId, ex);
        }
    }
}
