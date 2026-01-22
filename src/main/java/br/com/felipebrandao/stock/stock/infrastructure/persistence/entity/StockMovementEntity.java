package br.com.felipebrandao.stock.stock.infrastructure.persistence.entity;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_movement")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMovementEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 30)
    private StockMovementType type;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 3)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private LocationEntity fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private LocationEntity toLocation;

    @Column(name = "note", length = 1000)
    private String note;

    @Column(name = "occurred_at", nullable = false)
    private OffsetDateTime occurredAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;
}
