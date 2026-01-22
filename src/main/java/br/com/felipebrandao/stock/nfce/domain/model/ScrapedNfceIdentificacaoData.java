package br.com.felipebrandao.stock.nfce.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ScrapedNfceIdentificacaoData {

    private String chaveAcesso;
    private Integer modelo;
    private Integer serie;
    private Integer numero;
    private LocalDate dataEmissao;
    private LocalDate dataSaidaEntrada;
    private BigDecimal valorTotalNota;
}
