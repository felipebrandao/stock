package br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapedNfceResponse {

    private String uf;
    private String fonte;
    private Instant scrapedAt;

    private ScrapedNfceIdentificacaoResponse identificacao;
    private ScrapedNfceEmitenteResponse emitente;
    private List<ScrapedNfceItemResponse> itens;
}
