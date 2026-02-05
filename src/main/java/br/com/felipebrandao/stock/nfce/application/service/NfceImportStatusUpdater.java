package br.com.felipebrandao.stock.nfce.application.service;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NfceImportStatusUpdater {

    private final NfceImportRepository repository;

    @Transactional
    public void updateToProcessing(NfceImport nfceImport) {
        nfceImport.incrementAttempts();
        nfceImport.markProcessing();
        repository.save(nfceImport);

        log.info("[async-processor] Status atualizado para PROCESSING. id={} attempts={}",
                nfceImport.getId(), nfceImport.getAttempts());
    }
}
