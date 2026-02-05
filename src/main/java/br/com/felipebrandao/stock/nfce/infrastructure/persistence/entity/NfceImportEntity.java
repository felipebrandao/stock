package br.com.felipebrandao.stock.nfce.infrastructure.persistence.entity;

import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "nfce")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NfceImportEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "access_key", nullable = false, unique = true, length = 44)
    private String accessKey;

    @Column(name = "qr_code_url", nullable = false, length = 500)
    private String qrCodeUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private NfceStatus status;

    @Column(name = "error_message", length = 1000)
    private String errorMessage;

    @Column(name = "attempts", nullable = false)
    private Integer attempts;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;
}
