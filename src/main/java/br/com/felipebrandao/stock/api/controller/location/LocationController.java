package br.com.felipebrandao.stock.api.controller.location;

import br.com.felipebrandao.stock.api.controller.location.dto.request.CreateLocationRequest;
import br.com.felipebrandao.stock.api.controller.location.dto.response.LocationResponse;
import br.com.felipebrandao.stock.location.application.usecase.CreateLocationUseCase;
import br.com.felipebrandao.stock.location.application.usecase.DeleteLocationUseCase;
import br.com.felipebrandao.stock.location.application.usecase.GetLocationUseCase;
import br.com.felipebrandao.stock.location.application.usecase.ListLocationsUseCase;
import br.com.felipebrandao.stock.location.domain.model.Location;
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
@RequestMapping("/api/locations")
@RequiredArgsConstructor
public class LocationController {

    private final CreateLocationUseCase createLocationUseCase;
    private final ListLocationsUseCase listLocationsUseCase;
    private final GetLocationUseCase getLocationUseCase;
    private final DeleteLocationUseCase deleteLocationUseCase;

    @PostMapping
    public ResponseEntity<LocationResponse> create(@Valid @RequestBody CreateLocationRequest request) {
        UUID id = createLocationUseCase.execute(request.getName(), request.getDescription());
        Location created = getLocationUseCase.execute(id);
        return ResponseEntity.ok(toResponse(created));
    }

    @GetMapping
    public ResponseEntity<Page<LocationResponse>> list(Pageable pageable) {
        Page<LocationResponse> response = listLocationsUseCase.execute(pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getLocationUseCase.execute(id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        deleteLocationUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    private LocationResponse toResponse(Location location) {
        return LocationResponse.builder()
                .id(location.getId())
                .name(location.getName())
                .description(location.getDescription())
                .active(location.isActive())
                .createdAt(location.getCreatedAt())
                .build();
    }
}
