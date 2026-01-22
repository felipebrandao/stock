package br.com.felipebrandao.stock.stock.application.usecase;

import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import br.com.felipebrandao.stock.stock.domain.repository.StockMovementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ListStockMovementsUseCase {

    private final StockMovementRepository repository;

    public Page<StockMovement> execute(UUID productId, UUID locationId, Pageable pageable) {
        if (productId != null) {
            return repository.findAllByProductId(productId, pageable);
        }
        if (locationId != null) {
            return repository.findAllByLocationId(locationId, pageable);
        }
        return repository.findAll(pageable);
    }
}
