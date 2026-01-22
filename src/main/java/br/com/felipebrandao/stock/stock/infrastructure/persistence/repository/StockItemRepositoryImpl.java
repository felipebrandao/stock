package br.com.felipebrandao.stock.stock.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.stock.domain.model.StockItem;
import br.com.felipebrandao.stock.stock.domain.model.StockItemState;
import br.com.felipebrandao.stock.stock.domain.repository.StockItemRepository;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.entity.StockItemEntity;
import br.com.felipebrandao.stock.stock.infrastructure.persistence.mapper.StockItemMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class StockItemRepositoryImpl implements StockItemRepository {

    private final StockItemJpaRepository jpaRepository;
    private final StockItemMapper mapper;

    @Override
    public StockItem save(StockItem item) {
        StockItemEntity entity = mapper.toEntity(item);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<StockItem> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<StockItem> findAllByFilter(UUID productId, UUID locationId, StockItemState state, Pageable pageable) {
        if (state == null) {
            if (productId != null && locationId != null) {
                return jpaRepository.findAllByProduct_IdAndLocation_Id(productId, locationId, pageable).map(mapper::toDomain);
            }
            if (productId != null) {
                return jpaRepository.findAllByProduct_Id(productId, pageable).map(mapper::toDomain);
            }
            if (locationId != null) {
                return jpaRepository.findAllByLocation_Id(locationId, pageable).map(mapper::toDomain);
            }
            return jpaRepository.findAll(pageable).map(mapper::toDomain);
        }

        if (productId != null && locationId != null) {
            return jpaRepository.findAllByProduct_IdAndLocation_IdAndState(productId, locationId, state, pageable).map(mapper::toDomain);
        }
        if (productId != null) {
            return jpaRepository.findAllByProduct_IdAndState(productId, state, pageable).map(mapper::toDomain);
        }
        if (locationId != null) {
            return jpaRepository.findAllByLocation_IdAndState(locationId, state, pageable).map(mapper::toDomain);
        }
        return jpaRepository.findAllByState(state, pageable).map(mapper::toDomain);
    }
}
