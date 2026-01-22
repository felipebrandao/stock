package br.com.felipebrandao.stock.nfce.infrastructure.persistence.service;

import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper.NfceScrapeMapper;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.repository.NfceScrapeJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NfceScrapePersistenceService {

    private final NfceScrapeJpaRepository repository;
    private final NfceScrapeMapper mapper;

    public void save(ScrapedNfceData data) {
        if (data == null || data.getScrapedNfceIdentificacaoData() == null) return;

        String accessKey = data.getScrapedNfceIdentificacaoData().getChaveAcesso();
        if (accessKey == null || accessKey.isBlank()) return;

        if (repository.existsByAccessKey(accessKey)) {
            return;
        }

        NfceScrapeEntity entity = mapper.toEntity(data);

        if (entity.getEmitente() != null) {
            entity.getEmitente().setScrape(entity);
        }
        if (entity.getItens() != null) {
            entity.getItens().forEach(i -> i.setScrape(entity));
        }

        repository.save(entity);
    }
}
