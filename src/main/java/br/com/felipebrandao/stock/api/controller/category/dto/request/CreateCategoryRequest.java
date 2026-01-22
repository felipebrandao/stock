package br.com.felipebrandao.stock.api.controller.category.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCategoryRequest {

    @NotBlank(message = "name é obrigatório")
    private String name;
}
