package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessPendingNfceImportsUseCase {

    private final NfceImportRepository repository;
    private final ProcessNfceImportUseCase processNfceImportUseCase;

    @Transactional
    public void execute() {

        List<NfceImport> pendings =
                repository.findNextPending(5);

        log.info("[nfce-batch] Encontradas {} importações NFC-e pendentes para processar", pendings.size());

        int processed = 0;
        for (NfceImport nfceImport : pendings) {
            log.info("[nfce-batch] Processando importação {}/{}: id={}, qrCodeUrl={}",
                    ++processed, pendings.size(), nfceImport.getId(), nfceImport.getQrCodeUrl());
            nfceImport.markProcessing();
            repository.save(nfceImport);
            processNfceImportUseCase.execute(nfceImport);
        }

        log.info("[nfce-batch] Processamento em lote concluído. Total processado: {}", processed);
    }
}
