package br.com.felipebrandao.stock.category.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.category.domain.model.Category;
import br.com.felipebrandao.stock.category.infrastructure.persistence.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    Category toDomain(CategoryEntity entity);

    @Mapping(target = "id", source = "id")
    CategoryEntity toEntity(Category domain);

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
