package br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "nfce_scrape_emitente")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceScrapeEmitenteEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrape_id", nullable = false, unique = true)
    private NfceScrapeEntity scrape;

    @Column(name = "razao_social", length = 200)
    private String razaoSocial;

    @Column(name = "nome_fantasia", length = 200)
    private String nomeFantasia;

    @Column(name = "cnpj", length = 14)
    private String cnpj;

    @Column(name = "endereco", length = 300)
    private String endereco;

    @Column(name = "bairro", length = 120)
    private String bairro;

    @Column(name = "cep", length = 8)
    private String cep;

    @Column(name = "municipio", length = 120)
    private String municipio;

    @Column(name = "uf", length = 2)
    private String uf;

    @Column(name = "telefone", length = 30)
    private String telefone;
}
