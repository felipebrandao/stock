package br.com.felipebrandao.stock.api.controller.stock;

import br.com.felipebrandao.stock.api.controller.stock.dto.request.ConsumeStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.ConvertStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.OpenStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.request.TransferStockItemRequest;
import br.com.felipebrandao.stock.api.controller.stock.dto.response.StockItemResponse;
import br.com.felipebrandao.stock.stock.application.usecase.ConsumeStockItemUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.ConvertStockItemUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.GetStockItemUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.ListStockItemsUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.OpenStockItemUseCase;
import br.com.felipebrandao.stock.stock.application.usecase.TransferStockItemUseCase;
import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/stock-items")
@RequiredArgsConstructor
public class StockItemController {

    private final ListStockItemsUseCase listStockItemsUseCase;
    private final GetStockItemUseCase getStockItemUseCase;
    private final OpenStockItemUseCase openStockItemUseCase;
    private final TransferStockItemUseCase transferStockItemUseCase;
    private final ConsumeStockItemUseCase consumeStockItemUseCase;
    private final ConvertStockItemUseCase convertStockItemUseCase;

    @GetMapping
    public ResponseEntity<Page<StockItemResponse>> list(
            @RequestParam(required = false) UUID productId,
            @RequestParam(required = false) UUID locationId,
            @RequestParam(required = false) br.com.felipebrandao.stock.stock.domain.model.StockItemState state,
            Pageable pageable
    ) {
        Page<StockItemResponse> response = listStockItemsUseCase.execute(productId, locationId, state, pageable)
                .map(this::toResponse);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StockItemResponse> get(@PathVariable UUID id) {
        return ResponseEntity.ok(toResponse(getStockItemUseCase.execute(id)));
    }

    @PostMapping("/{id}/open")
    public ResponseEntity<StockItemResponse> open(@PathVariable UUID id, @Valid @RequestBody OpenStockItemRequest request) {
        UUID openedId = openStockItemUseCase.execute(id, request.getQuantity(), request.getTargetLocationId(), request.getNote());
        return ResponseEntity.ok(toResponse(getStockItemUseCase.execute(openedId)));
    }

    @PostMapping("/{id}/transfer")
    public ResponseEntity<Void> transfer(@PathVariable UUID id, @Valid @RequestBody TransferStockItemRequest request) {
        transferStockItemUseCase.execute(id, request.getTargetLocationId(), request.getNote());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/consume")
    public ResponseEntity<Void> consume(@PathVariable UUID id, @Valid @RequestBody ConsumeStockItemRequest request) {
        consumeStockItemUseCase.execute(id, request.getQuantity(), request.getNote());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/convert")
    public ResponseEntity<List<UUID>> convert(@PathVariable UUID id, @Valid @RequestBody ConvertStockItemRequest request) {
        return ResponseEntity.ok(convertStockItemUseCase.execute(id, request.getDestinationQuantities(), request.getNote()));
    }

    private StockItemResponse toResponse(StockItem item) {
        return StockItemResponse.builder()
                .id(item.getId())
                .productId(item.getProductId())
                .locationId(item.getLocationId())
                .quantity(item.getQuantity())
                .state(item.getState())
                .expiryDate(item.getExpiryDate())
                .createdAt(item.getCreatedAt())
                .updatedAt(item.getUpdatedAt())
                .build();
    }
}
