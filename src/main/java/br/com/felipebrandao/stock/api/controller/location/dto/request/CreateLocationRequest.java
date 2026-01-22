package br.com.felipebrandao.stock.api.controller.location.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateLocationRequest {

    @NotBlank
    private String name;

    private String description;
}
