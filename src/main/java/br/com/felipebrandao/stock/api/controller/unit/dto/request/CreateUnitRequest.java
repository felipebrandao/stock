package br.com.felipebrandao.stock.api.controller.unit.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateUnitRequest {

    @NotBlank(message = "name é obrigatório")
    private String name;

    @NotBlank(message = "abbreviation é obrigatório")
    private String abbreviation;
}
