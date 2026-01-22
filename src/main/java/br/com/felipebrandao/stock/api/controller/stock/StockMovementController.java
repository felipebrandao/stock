package br.com.felipebrandao.stock.api.controller.stock;

import br.com.felipebrandao.stock.api.controller.stock.dto.request.CreateStockMovementRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.response.StockMovementResponse;
import br.com.felipebrandao.stock.stock.application.usecase.CreateStockMovementUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.ListStockMovementsUseCase;
import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/stock-movements")
@RequiredArgsConstructor
public class StockMovementController {

    private final CreateStockMovementUseCase createStockMovementUseCase;
    private final ListStockMovementsUseCase listStockMovementsUseCase;

    @PostMapping
    public ResponseEntity<StockMovementResponse> create(@Valid @RequestBody CreateStockMovementRequest request) {
        UUID id = createStockMovementUseCase.execute(
                request.getType(),
                request.getProductId(),
                request.getQuantity(),
                request.getFromLocationId(),
                request.getToLocationId(),
                request.getNote(),
                request.getOccurredAt()
        );

        // For now, return just the id. (Client can query via GET)
        return ResponseEntity.ok(StockMovementResponse.builder().id(id).build());
    }

    @GetMapping
    public ResponseEntity<Page<StockMovementResponse>> list(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID locationId,
            Pageable pageable
    ) {
        Page<StockMovementResponse> response = listStockMovementsUseCase.execute(productId, locationId, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    private StockMovementResponse toResponse(StockMovement movement) {
        return StockMovementResponse.builder()
                .id(movement.getId())
                .type(movement.getType())
                .productId(movement.getProductId())
                .quantity(movement.getQuantity())
                .fromLocationId(movement.getFromLocationId())
                .toLocationId(movement.getToLocationId())
                .note(movement.getNote())
                .occurredAt(movement.getOccurredAt())
                .createdAt(movement.getCreatedAt())
                .build();
    }
}
