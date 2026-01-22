package br.com.felipebrandao.stock.nfce.infrastructure.scraper.adapter;

import br.com.felipebrandao.stock.nfce.application.port.out.NfceScraperClient;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapeNfceRequest;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceResponse;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.feign.NfceScraperFeignClient;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.mapper.ScrapedNfceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NfceScraperClientImpl implements NfceScraperClient {

    private final NfceScraperFeignClient feign;
    private final ScrapedNfceMapper mapper;

    @Override
    public ScrapedNfceData scrape(String qrCodeUrl) {
        ScrapedNfceResponse response = feign.scrape(new ScrapeNfceRequest(qrCodeUrl));
        return mapper.toDomain(response);
    }
}
