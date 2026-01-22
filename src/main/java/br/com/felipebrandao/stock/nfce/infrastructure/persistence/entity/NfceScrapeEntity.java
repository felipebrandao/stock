package br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "nfce_scrape")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceScrapeEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "access_key", nullable = false, unique = true, length = 44)
    private String accessKey;

    @Column(name = "uf", nullable = false, length = 2)
    private String uf;

    @Column(name = "fonte", nullable = false, length = 100)
    private String fonte;

    @Column(name = "scraped_at", nullable = false)
    private OffsetDateTime scrapedAt;

    @Column(name = "modelo")
    private Integer modelo;

    @Column(name = "serie")
    private Integer serie;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "data_emissao")
    private java.time.LocalDate dataEmissao;

    @Column(name = "data_saida_entrada")
    private java.time.LocalDate dataSaidaEntrada;

    @Column(name = "valor_total_nota", precision = 19, scale = 2)
    private java.math.BigDecimal valorTotalNota;

    @OneToOne(mappedBy = "scrape", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private NfceScrapeEmitenteEntity emitente;

    @OneToMany(mappedBy = "scrape", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<NfceScrapeItemEntity> itens = new ArrayList<>();

    public void setEmitente(NfceScrapeEmitenteEntity emitente) {
        this.emitente = emitente;
        if (emitente != null) {
            emitente.setScrape(this);
        }
    }

    public void addItem(NfceScrapeItemEntity item) {
        if (item == null) return;
        item.setScrape(this);
        this.itens.add(item);
    }
}
