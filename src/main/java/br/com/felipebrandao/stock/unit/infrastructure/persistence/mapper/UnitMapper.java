package br.com.felipebrandao.stock.unit.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import br.com.felipebrandao.stock.unit.infrastructure.persistence.entity.UnitEntity;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    Unit toDomain(UnitEntity entity);

    UnitEntity toEntity(Unit domain);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}

