package br.com.felipebrandao.stock.nfce.domain.model;

import br.com.felipebrandao.stock.nfce.domain.model.enums.NfceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class NfceImport {

    private UUID id;
    private String qrCodeUrl;
    private String accessKey;
    private NfceStatus status;
    private Instant createdAt;
    private Instant updatedAt;
    private String errorMessage;
    private Integer attempts;

    public static NfceImport createPending(String qrCodeUrl, String accessKey) {

        return new NfceImport(
                null,
                qrCodeUrl,
                accessKey,
                NfceStatus.PENDING,
                Instant.now(),
                null,
                null,
                0
        );
    }

    public void markProcessing() {
        this.status = NfceStatus.PROCESSING;
        this.updatedAt = Instant.now();
    }

    public void markCompleted() {
        this.status = NfceStatus.COMPLETED;
        this.updatedAt = Instant.now();
    }

    public void markFailed(String reason) {
        this.status = NfceStatus.ERROR;
        this.errorMessage = reason;
        this.updatedAt = Instant.now();
    }

    public void incrementAttempts() {
        this.attempts = (this.attempts == null ? 0 : this.attempts) + 1;
    }
}
