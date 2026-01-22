package br.com.felipebrandao.stock.api.scheduler;

import br.com.felipebrandao.stock.nfce.application.usecase.ProcessPendingNfceImportsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NfceImportScheduler {

    private final ProcessPendingNfceImportsUseCase processPendingNfceImportsUseCase;

    @Scheduled(fixedDelay = 5000)
    public void processPendingImports() {
        try {
            log.debug("[scheduler] Processando importações NFC-e pendentes...");
            processPendingNfceImportsUseCase.execute();
        } catch (Exception e) {
            log.error("[scheduler] Erro ao processar importações NFC-e pendentes", e);
        }
    }
}
