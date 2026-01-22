package br.com.felipebrandao.stock.product.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductAliasEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductAliasJpaRepository extends JpaRepository<ProductAliasEntity, UUID> {

    Optional<ProductAliasEntity> findByAliasNormalized(String aliasNormalized);

    Optional<ProductAliasEntity> findByEan(String ean);
}
