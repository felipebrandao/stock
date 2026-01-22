package br.com.felipebrandao.stock.nfce.application.port.out;

import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;

public interface NfceScraperClient {
    ScrapedNfceData scrape(String qrCodeUrl);
}

