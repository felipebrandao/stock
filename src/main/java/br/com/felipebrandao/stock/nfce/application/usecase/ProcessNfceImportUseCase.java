package br.com.felipebrandao.stock.nfce.application.usecase;

import br.com.felipebrandao.stock.nfce.application.port.out.NfceScraperClient;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.domain.repository.NfceImportRepository;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.service.NfceScrapePersistenceService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProcessNfceImportUseCase {

    private final NfceImportRepository repository;
    private final NfceScraperClient scraperClient;
    private final NfceScrapePersistenceService scrapePersistenceService;

    @Transactional
    public void execute(NfceImport nfceImport) {

        try {
            ScrapedNfceData data = scraperClient.scrape(nfceImport.getQrCodeUrl());

            scrapePersistenceService.save(data);

            nfceImport.markCompleted();
            repository.save(nfceImport);
        } catch (Exception ex) {
            nfceImport.markFailed(ex.getMessage());
            repository.save(nfceImport);
        }
    }
}
