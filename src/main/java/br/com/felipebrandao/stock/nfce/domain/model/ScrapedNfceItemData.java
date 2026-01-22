package br.com.felipebrandao.stock.nfce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ScrapedNfceItemData {

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
