package br.com.felipebrandao.stock.product.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductAlias {

    private UUID id;
    private String aliasNormalized;
    private String ean;
    private UUID productId;
    private Instant createdAt;

    public static ProductAlias create(String aliasNormalized, UUID productId) {
        return create(aliasNormalized, null, productId);
    }

    public static ProductAlias create(String aliasNormalized, String ean, UUID productId) {
        return new ProductAlias(
                null,
                aliasNormalized,
                ean == null ? null : ean.trim(),
                productId,
                Instant.now()
        );
    }
}
