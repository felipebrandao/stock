package br.com.felipebrandao.stock.api.controller.category.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class CategoryResponse {
    private UUID id;
    private String name;
    private Instant createdAt;
}
