package br.com.felipebrandao.stock.stock.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.stock.domain.model.StockMovement;
import br.com.felipebrandao.stock.stock.domain.repository.StockMovementRepository;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockMovementEntity;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.mapper.StockMovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StockMovementRepositoryImpl implements StockMovementRepository {

    private final StockMovementJpaRepository jpaRepository;
    private final StockMovementMapper mapper;

    @Override
    public StockMovement save(StockMovement movement) {
        StockMovementEntity entity = mapper.toEntity(movement);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Page<StockMovement> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public Page<StockMovement> findAllByProductId(UUID productId, Pageable pageable) {
        return jpaRepository.findAllByProduct_Id(productId, pageable).map(mapper::toDomain);
    }

    @Override
    public Page<StockMovement> findAllByLocationId(UUID locationId, Pageable pageable) {
        return jpaRepository.findAllByFromLocation_IdOrToLocation_Id(locationId, locationId, pageable).map(mapper::toDomain);
    }
}
