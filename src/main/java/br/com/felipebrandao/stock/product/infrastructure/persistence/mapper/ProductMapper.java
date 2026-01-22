package br.com.felipebrandao.stock.product.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.category.infrastructure.persistence.entity.CategoryEntity;
import br.com.felipebrandao.stock.product.domain.model.Product;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    Product toDomain(ProductEntity entity);

    @Mapping(target = "category", expression = "java(categoryRef(domain.getCategoryId()))")
    ProductEntity toEntity(Product domain);

    default CategoryEntity categoryRef(UUID id) {
        if (id == null) {
            return null;
        }
        CategoryEntity category = new CategoryEntity();
        category.setId(id);
        return category;
    }

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
