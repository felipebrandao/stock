package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.stock.domain.model.StockMovementType;
import br.com.felipebrandao.stock.stock.domain.repository.StockItemRepository;
import br.com.felipebrandao.stock.stock.domain.repository.StockMovementRepository;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateStockMovementUseCaseTest {

    private final StockMovementRepository movementRepository = mock(StockMovementRepository.class);
    private final StockItemRepository stockItemRepository = mock(StockItemRepository.class);
    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final LocationRepository locationRepository = mock(LocationRepository.class);

    private final CreateStockMovementUseCase useCase = new CreateStockMovementUseCase(
            movementRepository,
            stockItemRepository,
            productRepository,
            locationRepository
    );

    @Test
    void shouldCreateInMovementAndCreateClosedLot() {
        UUID productId = UUID.randomUUID();
        UUID toLocationId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct()));
        when(locationRepository.findById(toLocationId)).thenReturn(Optional.of(mockLocation()));

        when(movementRepository.save(any())).thenAnswer(inv -> {
            var m = inv.getArgument(0, br.com.felipebrandao.stock.stock.domain.model.StockMovement.class);
            m.setId(UUID.randomUUID());
            return m;
        });

        assertDoesNotThrow(() -> useCase.execute(
                StockMovementType.IN,
                productId,
                new BigDecimal("2"),
                null,
                toLocationId,
                "entry",
                null
        ));

        verify(stockItemRepository).save(any());
        verify(movementRepository).save(any());
    }

    @Test
    void shouldRejectOutAtGenericMovementEndpoint() {
        UUID productId = UUID.randomUUID();
        UUID fromLocationId = UUID.randomUUID();

        when(productRepository.findById(productId)).thenReturn(Optional.of(mockProduct()));
        when(locationRepository.findById(fromLocationId)).thenReturn(Optional.of(mockLocation()));

        assertThrows(BusinessException.class, () -> useCase.execute(
                StockMovementType.OUT,
                productId,
                new BigDecimal("1"),
                fromLocationId,
                null,
                null,
                null
        ));

        verify(movementRepository, never()).save(any());
        verify(stockItemRepository, never()).save(any());
    }

    private br.com.felipebrandao.stock.product.domain.model.Product mockProduct() {
        return new br.com.felipebrandao.stock.product.domain.model.Product(UUID.randomUUID(), "p", UUID.randomUUID(), java.time.Instant.now());
    }

    private br.com.felipebrandao.stock.location.domain.model.Location mockLocation() {
        return new br.com.felipebrandao.stock.location.domain.model.Location(UUID.randomUUID(), "l", null, true, java.time.Instant.now());
    }
}
