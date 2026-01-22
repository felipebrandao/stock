package br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScrapedNfceItemResponse {
    private Integer numero;
    private String descricao;
    private BigDecimal quantidade;
    private String unidade;
    private BigDecimal valor;
    private BigDecimal valorUnitario;
    private String ncm;
    private String cest;
    private String cfop;
    private String ean;
}

