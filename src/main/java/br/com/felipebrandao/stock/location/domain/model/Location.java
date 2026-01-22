package br.com.felipebrandao.stock.location.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Location {

    private UUID id;
    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;

    public static Location create(String name, String description) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome do local é obrigatório");
        }

        String normalizedDescription = description == null ? null : description.trim();

        return new Location(
                null,
                normalizedName,
                normalizedDescription,
                true,
                Instant.now()
        );
    }
}
