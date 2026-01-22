package br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity;

import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceItemStatus;
import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.product.infrastructure.persistence.entity.ProductEntity;
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
@Table(name = "nfce_import_item")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceImportItemEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nfce_import_id", nullable = false)
    private NfceImportEntity nfceImport;

    @Column(name = "item_number")
    private Integer itemNumber;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "description_normalized", nullable = false, length = 500)
    private String descriptionNormalized;

    @Column(name = "ean", length = 30)
    private String ean;

    @Column(name = "unit", length = 20)
    private String unit;

    @Column(name = "quantity", nullable = false, precision = 19, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 19, scale = 6)
    private BigDecimal unitPrice;

    @Column(name = "total_price", precision = 19, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private NfceItemStatus status;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private LocationEntity location;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
