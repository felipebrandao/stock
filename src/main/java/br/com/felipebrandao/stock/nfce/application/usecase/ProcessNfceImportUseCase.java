package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.application.port.out.NfceScraperClient;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.service.NfceScrapePersistenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessNfceImportUseCase {

    private final NfceImportRepository repository;
    private final NfceScraperClient scraperClient;
    private final NfceScrapePersistenceService scrapePersistenceService;


    public void execute(NfceImport nfceImport) {
        long start = System.currentTimeMillis();
        String qr = nfceImport.getQrCodeUrl();
        try {
            log.info("[nfce-import] Iniciando processamento da importação NFC-e. qrCodeUrl={}", qr);

            log.info("[nfce-import] Chamando scraper para qrCodeUrl={}", qr);
            long scraperStart = System.currentTimeMillis();
            ScrapedNfceData data = scraperClient.scrape(qr);
            long scraperDuration = System.currentTimeMillis() - scraperStart;
            log.info("[nfce-import] Scraper concluído com sucesso em {} ms para qrCodeUrl={}", scraperDuration, qr);

            persistSuccess(nfceImport, data);

            long duration = System.currentTimeMillis() - start;
            log.info("[nfce-import] Importação NFC-e processada com sucesso. qrCodeUrl={} durationMs={}", qr, duration);
        } catch (Exception ex) {
            persistFailure(nfceImport, ex.getMessage());
            long duration = System.currentTimeMillis() - start;
            log.error("[nfce-import] Erro ao processar importação NFC-e. qrCodeUrl={} after {} ms", qr, duration, ex);
        }
    }

    @Transactional
    protected void persistSuccess(NfceImport nfceImport, ScrapedNfceData data) {
        scrapePersistenceService.save(data);
        nfceImport.markCompleted();
        repository.save(nfceImport);
    }

    @Transactional
    protected void persistFailure(NfceImport nfceImport, String errorMessage) {
        nfceImport.markFailed(errorMessage);
        repository.save(nfceImport);
    }
}
