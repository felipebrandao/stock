package br.com.felipebrandao.stock.nfce.infrastructure.persistence.mapper;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.nfce.domain.model.NfceImportItem;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportEntity;
import br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity.NfceImportItemEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface NfceImportItemMapper {

    @Mapping(target = "nfceImport", expression = "java(nfceImportEntity(domain.getNfceImportId()))")
    @Mapping(target = "product", expression = "java(productEntity(domain.getProductId()))")
    @Mapping(target = "location", expression = "java(locationEntity(domain.getLocationId()))")
    @Mapping(target = "createdAt", expression = "java(toOffset(domain.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(toOffset(domain.getUpdatedAt()))")
    NfceImportItemEntity toEntity(NfceImportItem domain);

    @Mapping(target = "nfceImportId", source = "nfceImport.id")
    @Mapping(target = "productId", source = "product.id")
    @Mapping(target = "locationId", source = "location.id")
    @Mapping(target = "createdAt", expression = "java(toInstant(entity.getCreatedAt()))")
    @Mapping(target = "updatedAt", expression = "java(toInstant(entity.getUpdatedAt()))")
    NfceImportItem toDomain(NfceImportItemEntity entity);

    default NfceImportEntity nfceImportEntity(UUID id) {
        if (id == null) return null;
        NfceImportEntity e = new NfceImportEntity();
        e.setId(id);
        return e;
    }

    default ProductEntity productEntity(UUID id) {
        if (id == null) return null;
        ProductEntity p = new ProductEntity();
        p.setId(id);
        return p;
    }

    default LocationEntity locationEntity(UUID id) {
        if (id == null) return null;
        LocationEntity l = new LocationEntity();
        l.setId(id);
        return l;
    }

    default OffsetDateTime toOffset(Instant instant) {
        return instant == null ? null : instant.atOffset(ZoneOffset.UTC);
    }

    default Instant toInstant(OffsetDateTime odt) {
        return odt == null ? null : odt.toInstant();
    }
}
