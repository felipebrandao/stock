package br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto;

import java.time.LocalDate;
import java.math.BigDecimal;

import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScrapedNfceIdentificacaoResponse {

    private String chaveAcesso;
    private Integer modelo;
    private Integer serie;
    private Integer numero;
    private LocalDate dataEmissao;
    private LocalDate dataSaidaEntrada;
    private BigDecimal valorTotalNota;

}


