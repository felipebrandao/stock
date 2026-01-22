package br.com.felipebrandao.stock.product.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.product.domain.model.ProductAlias;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductAliasEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface ProductAliasMapper {

    @Mapping(target = "product", expression = "java(productEntity(domain.getProductId()))")
    @Mapping(target = "createdAt", expression = "java(toOffset(domain.getCreatedAt()))")
    ProductAliasEntity toEntity(ProductAlias domain);

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "createdAt", expression = "java(toInstant(entity.getCreatedAt()))")
    ProductAlias toDomain(ProductAliasEntity entity);

    default ProductEntity productEntity(java.util.UUID id) {
        if (id == null) return null;
        ProductEntity p = new ProductEntity();
        p.setId(id);
        return p;
    }

    default OffsetDateTime toOffset(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    default Instant toInstant(OffsetDateTime odt) {
        return odt == null ? null : odt.toInstant();
    }
}
