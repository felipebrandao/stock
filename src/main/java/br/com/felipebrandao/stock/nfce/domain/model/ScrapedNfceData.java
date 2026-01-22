package br.com.felipebrandao.stock.nfce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
public class ScrapedNfceData {

    private String uf;
    private String fonte;
    private Instant scrapedAt;
    private ScrapedNfceIdentificacaoData scrapedNfceIdentificacaoData;
    private ScrapedNfceEmitenteData scrapedNfceEmitenteData;
    private List<ScrapedNfceItemData> scrapedNfceItemData;

}
