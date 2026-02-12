package br.com.felipebrandao.stock.unit.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Unit {

    private UUID id;
    private String name;
    private String abbreviation;
    private Instant createdAt;

    public static Unit create(String name, String abbreviation) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome da unidade é obrigatório");
        }

        String normalizedAbbreviation = abbreviation == null ? null : abbreviation.trim().toUpperCase();
        if (normalizedAbbreviation == null || normalizedAbbreviation.isBlank()) {
            throw new BusinessException("Abreviação da unidade é obrigatória");
        }

        return new Unit(
                null,
                normalizedName,
                normalizedAbbreviation,
                Instant.now()
        );
    }
}

