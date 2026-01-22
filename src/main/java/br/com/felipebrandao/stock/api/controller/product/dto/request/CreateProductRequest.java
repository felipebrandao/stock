package br.com.felipebrandao.stock.api.controller.product.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateProductRequest {

    @NotBlank(message = "name é obrigatório")
    private String name;

    @NotNull(message = "categoryId é obrigatório")
    private UUID categoryId;
}
