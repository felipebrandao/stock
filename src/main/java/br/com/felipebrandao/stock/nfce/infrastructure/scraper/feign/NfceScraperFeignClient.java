package br.com.felipebrandao.stock.nfce.infrastructure.scraper.feign;

import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapeNfceRequest;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "nfce-scraper",
        url = "${nfce.scraper.url}"
)
public interface NfceScraperFeignClient {

    @PostMapping("/scrape")
    ScrapedNfceResponse scrape(@RequestBody ScrapeNfceRequest request);
}
