package br.com.felipebrandao.stock.api.scheduler;

import br.com.felipebrandao.stock.nfce.application.usecase.ProcessPendingNfceImportsUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class NfceImportScheduler {

    private final ProcessPendingNfceImportsUseCase processPendingNfceImportsUseCase;

    @Value("${scheduler.nfce.import.fixedDelayMs:300000}")
    private long fixedDelayMs;

    @Scheduled(fixedDelayString = "${scheduler.nfce.import.fixedDelayMs:300000}")
    public void processPendingImports() {
        long start = System.currentTimeMillis();
        try {
            log.info("[scheduler] Iniciando processamento de importações NFC-e pendentes...");
            processPendingNfceImportsUseCase.execute();
            long duration = System.currentTimeMillis() - start;
            log.info("[scheduler] Processamento de importações NFC-e pendentes concluído com sucesso em {} ms.", duration);
        } catch (Exception e) {
            long duration = System.currentTimeMillis() - start;
            log.error("[scheduler] Erro ao processar importações NFC-e pendentes após {} ms", duration, e);
        }
    }
}
