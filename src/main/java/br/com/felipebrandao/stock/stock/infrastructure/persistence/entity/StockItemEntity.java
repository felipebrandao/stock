package br.com.felipebrandao.stock.stock.infrastructure.persistence.entity;

import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
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
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "stock_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockItemEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "location_id", nullable = false)
    private LocationEntity location;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 30)
    private StockItemState state;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
