package br.com.felipebrandao.stock.stock.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StockItemMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "state", source = "entity.state")
    @Mapping(target = "expiryDate", source = "entity.expiryDate")
    StockItem toDomain(StockItemEntity entity);

    @Mapping(target = "product", expression = "java(productRef(domain.getProductId()))")
    @Mapping(target = "location", expression = "java(locationRef(domain.getLocationId()))")
    StockItemEntity toEntity(StockItem domain);

    default ProductEntity productRef(UUID id) {
        if (id == null) {
            return null;
        }
        ProductEntity product = new ProductEntity();
        product.setId(id);
        return product;
    }

    default LocationEntity locationRef(UUID id) {
        if (id == null) {
            return null;
        }
        LocationEntity location = new LocationEntity();
        location.setId(id);
        return location;
    }

    default OffsetDateTime map(Instant value) {
        return value == null ? null : value.atOffset(ZoneOffset.UTC);
    }

    default Instant map(OffsetDateTime value) {
        return value == null ? null : value.toInstant();
    }
}
