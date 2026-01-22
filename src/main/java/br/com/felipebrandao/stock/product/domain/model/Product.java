package br.com.felipebrandao.stock.product.domain.model;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
public class Product {

    private UUID id;
    private String name;
    private UUID categoryId;
    private Instant createdAt;

    public static Product create(String name, UUID categoryId) {
        if (name == null || name.isBlank()) {
            throw new BusinessException("Nome do produto é obrigatório");
        }
        if (categoryId == null) {
            throw new BusinessException("Categoria do produto é obrigatória");
        }

        return new Product(
                null,
                name.trim(),
                categoryId,
                Instant.now()
        );
    }
}
