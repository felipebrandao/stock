package br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceEmitenteData;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceItemData;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEmitenteEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceScrapeItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface NfceScrapeMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "accessKey", source = "scrapedNfceIdentificacaoData.chaveAcesso")

    @Mapping(target = "modelo", source = "scrapedNfceIdentificacaoData.modelo")
    @Mapping(target = "serie", source = "scrapedNfceIdentificacaoData.serie")
    @Mapping(target = "numero", source = "scrapedNfceIdentificacaoData.numero")
    @Mapping(target = "dataEmissao", source = "scrapedNfceIdentificacaoData.dataEmissao")
    @Mapping(target = "dataSaidaEntrada", source = "scrapedNfceIdentificacaoData.dataSaidaEntrada")
    @Mapping(target = "valorTotalNota", source = "scrapedNfceIdentificacaoData.valorTotalNota")

    @Mapping(target = "emitente", source = "scrapedNfceEmitenteData")
    @Mapping(target = "itens", source = "scrapedNfceItemData")
    NfceScrapeEntity toEntity(ScrapedNfceData domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scrape", ignore = true)
    @Mapping(target = "cnpj", expression = "java(br.com.felipebrandao.stock.shared.utils.DocumentNormalizer.normalizeCnpj(domain.getCnpj()))")
    @Mapping(target = "cep", expression = "java(br.com.felipebrandao.stock.shared.utils.DocumentNormalizer.normalizeCep(domain.getCep()))")
    NfceScrapeEmitenteEntity toEntity(ScrapedNfceEmitenteData domain);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "scrape", ignore = true)
    NfceScrapeItemEntity toEntity(ScrapedNfceItemData domain);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
