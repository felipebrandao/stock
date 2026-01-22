package br.com.felipebrandao.stock.product.domain.repository;

import br.com.felipebrandao.stock.product.domain.model.ProductAlias;

import java.util.Optional;

public interface ProductAliasRepository {

    Optional<ProductAlias> findByAliasNormalized(String aliasNormalized);

    Optional<ProductAlias> findByEan(String ean);

    ProductAlias save(ProductAlias alias);
}
