package br.com.felipebrandao.stock.location.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.location.domain.model.Location;
import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import org.mapstruct.Mapper;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface LocationMapper {

    Location toDomain(LocationEntity entity);

    LocationEntity toEntity(Location domain);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
