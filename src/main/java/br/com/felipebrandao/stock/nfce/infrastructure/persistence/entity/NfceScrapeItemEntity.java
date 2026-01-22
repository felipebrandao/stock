package br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "nfce_scrape_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceScrapeItemEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrape_id", nullable = false)
    private NfceScrapeEntity scrape;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "quantidade", precision = 19, scale = 6)
    private BigDecimal quantidade;

    @Column(name = "unidade", length = 20)
    private String unidade;

    @Column(name = "valor", precision = 19, scale = 2)
    private BigDecimal valor;

    @Column(name = "valor_unitario", precision = 19, scale = 6)
    private BigDecimal valorUnitario;

    @Column(name = "ncm", length = 20)
    private String ncm;

    @Column(name = "cest", length = 20)
    private String cest;

    @Column(name = "cfop", length = 10)
    private String cfop;

    @Column(name = "ean", length = 30)
    private String ean;
}
