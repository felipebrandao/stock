package br.com.felipebrandao.stock.product.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_alias")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAliasEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "alias_normalized", nullable = false, length = 255, unique = true)
    private String aliasNormalized;

    @Column(name = "ean", length = 30, unique = true)
    private String ean;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
