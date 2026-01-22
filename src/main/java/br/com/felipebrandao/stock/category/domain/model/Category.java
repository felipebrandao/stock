package br.com.felipebrandao.stock.category.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Category {

    private UUID id;
    private String name;
    private Instant createdAt;

    public static Category create(String name) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Nome da categoria é obrigatório");
        }

        return new Category(
                null,
                name.trim(),
                Instant.now()
        );
    }
}
