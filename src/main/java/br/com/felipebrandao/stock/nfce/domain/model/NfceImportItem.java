package br.com.felipebrandao.stock.nfce.domain.model;

import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceItemStatus;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NfceImportItem {

    private UUID id;
    private UUID nfceImportId;

    private Integer itemNumber;
    private String description;
    private String descriptionNormalized;
    private String ean;
    private String unit;

    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

    private UUID productId;
    private NfceItemStatus status;

    private LocalDate expiryDate;
    private UUID locationId;

    private Instant createdAt;
    private Instant updatedAt;

    public static NfceImportItem create(
            UUID nfceImportId,
            Integer itemNumber,
            String description,
            String descriptionNormalized,
            String ean,
            String unit,
            BigDecimal quantity,
            BigDecimal unitPrice,
            BigDecimal totalPrice,
            UUID productId,
            NfceItemStatus status,
            LocalDate expiryDate,
            UUID locationId
    ) {
        if (nfceImportId == null) {
            throw new BusinessException("nfceImportId é obrigatório");
        }
        if (description == null || description.isBlank()) {
            throw new BusinessException("Descrição do item é obrigatória");
        }
        if (descriptionNormalized == null || descriptionNormalized.isBlank()) {
            throw new BusinessException("descriptionNormalized é obrigatório");
        }
        if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Quantidade deve ser > 0");
        }
        if (status == null) {
            throw new BusinessException("Status do item é obrigatório");
        }

        Instant now = Instant.now();
        return new NfceImportItem(
                null,
                nfceImportId,
                itemNumber,
                description,
                descriptionNormalized,
                ean,
                unit,
                quantity,
                unitPrice,
                totalPrice,
                productId,
                status,
                expiryDate,
                locationId,
                now,
                now
        );
    }

    public void updateReview(UUID productId, BigDecimal quantity, LocalDate expiryDate, UUID locationId) {
        if (quantity != null && quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BusinessException("Quantidade deve ser > 0");
        }
        if (locationId == null) {
            throw new BusinessException("Local é obrigatório");
        }

        this.productId = productId;
        this.quantity = quantity == null ? this.quantity : quantity;
        this.expiryDate = expiryDate;
        this.locationId = locationId;

        this.status = (this.productId == null) ? NfceItemStatus.NOT_MAPPED : NfceItemStatus.MAPPED;
        this.updatedAt = Instant.now();
    }
}
