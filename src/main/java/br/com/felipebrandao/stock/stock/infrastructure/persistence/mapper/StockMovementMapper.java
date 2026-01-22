package br.com.felipebrandao.stock.stock.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockMovementEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface StockMovementMapper {

    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "fromLocationId", source = "fromLocation.id")
    @Mapping(target = "toLocationId", source = "toLocation.id")
    StockMovement toDomain(StockMovementEntity entity);

    @Mapping(target = "product", expression = "java(productRef(domain.getProductId()))")
    @Mapping(target = "fromLocation", expression = "java(locationRef(domain.getFromLocationId()))")
    @Mapping(target = "toLocation", expression = "java(locationRef(domain.getToLocationId()))")
    StockMovementEntity toEntity(StockMovement domain);

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
