package br.com.felipebrandao.stock.api.controller.product.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class ProductResponse {
    private UUID id;
    private String name;
    private UUID categoryId;
    private Instant createdAt;
}
