package br.com.felipebrandao.stock.product.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.product.domain.model.ProductAlias;
import br.com.felipebrandao.stock.product.domain.repository.ProductAliasRepository;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductAliasEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.mapper.ProductAliasMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProductAliasRepositoryImpl implements ProductAliasRepository {

    private final ProductAliasJpaRepository jpaRepository;
    private final ProductAliasMapper mapper;

    @Override
    public Optional<ProductAlias> findByAliasNormalized(String aliasNormalized) {
        return jpaRepository.findByAliasNormalized(aliasNormalized)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<ProductAlias> findByEan(String ean) {
        if (ean == null || ean.isBlank()) {
            return Optional.empty();
        }
        return jpaRepository.findByEan(ean.trim())
                .map(mapper::toDomain);
    }

    @Override
    public ProductAlias save(ProductAlias alias) {
        ProductAliasEntity entity = mapper.toEntity(alias);
        ProductAliasEntity saved = jpaRepository.save(entity);
        return mapper.toDomain(saved);
    }
}
