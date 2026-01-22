package br.com.felipebrandao.stock.nfce.infrastructure.scraper.mapper;

import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceData;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceEmitenteData;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceIdentificacaoData;
import br.com.felipebrandao.stock.nfce.domain.model.ScrapedNfceItemData;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceEmitenteResponse;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceIdentificacaoResponse;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceItemResponse;
import br.com.felipebrandao.stock.nfce.infrastructure.scraper.dto.ScrapedNfceResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScrapedNfceMapper {

    @Mapping(target = "scrapedNfceIdentificacaoData", source = "identificacao")
    @Mapping(target = "scrapedNfceEmitenteData", source = "emitente")
    @Mapping(target = "scrapedNfceItemData", source = "itens")
    ScrapedNfceData toDomain(ScrapedNfceResponse response);

    ScrapedNfceIdentificacaoData toDomain(ScrapedNfceIdentificacaoResponse response);

    ScrapedNfceEmitenteData toDomain(ScrapedNfceEmitenteResponse response);

    ScrapedNfceItemData toDomain(ScrapedNfceItemResponse response);
}
