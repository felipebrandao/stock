package br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.nfce.domain.model.NfceImport;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface NfceImportMapper {

    @Mapping(target = "id", source = "id")
    NfceImport toDomain(NfceImportEntity entity);

    @Mapping(target = "id", source = "id")
    NfceImportEntity toEntity(NfceImport domain);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
