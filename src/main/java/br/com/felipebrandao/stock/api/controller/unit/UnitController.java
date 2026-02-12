package br.com.felipebrandao.stock.api.controller.unit;

import br.com.felipebrandao.stock.api.controller.unit.dto.request.CreateUnitRequest;
import br.com.felipebrandao.stock.api.controller.unit.dto.response.UnitResponse;
import br.com.felipebrandao.stock.unit.application.usecase.CreateUnitUseCase;
import br.com.felipebrandao.stock.unit.application.usecase.DeleteUnitUseCase;
import br.com.felipebrandao.stock.unit.application.usecase.GetUnitUseCase;
import br.com.felipebrandao.stock.unit.application.usecase.ListUnitsUseCase;
import br.com.felipebrandao.stock.unit.domain.model.Unit;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class UnitController {

    private final CreateUnitUseCase createUnitUseCase;
    private final ListUnitsUseCase listUnitsUseCase;
    private final GetUnitUseCase getUnitUseCase;
    private final DeleteUnitUseCase deleteUnitUseCase;

    @PostMapping
    public ResponseEntity<UnitResponse> create(@Valid @RequestBody CreateUnitRequest request) {
        UUID id = createUnitUseCase.execute(request.getName(), request.getAbbreviation());
        Unit created = getUnitUseCase.execute(id);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<UnitResponse>> list(Pageable pageable) {
        Page<UnitResponse> response = listUnitsUseCase.execute(pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnitResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getUnitUseCase.execute(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteUnitUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private UnitResponse toResponse(Unit unit) {
        return UnitResponse.builder()
                .id(unit.getId())
                .name(unit.getName())
                .abbreviation(unit.getAbbreviation())
                .createdAt(unit.getCreatedAt())
                .build();
    }
}
