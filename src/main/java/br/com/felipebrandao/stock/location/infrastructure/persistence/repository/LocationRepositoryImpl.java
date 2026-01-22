package br.com.felipebrandao.stock.location.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.location.domain.model.Location;
import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.location.infrastructure.persistence.entity.LocationEntity;
import br.com.felipebrandao.stock.location.infrastructure.persistence.mapper.LocationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LocationRepositoryImpl implements LocationRepository {

    private final LocationJpaRepository jpaRepository;
    private final LocationMapper mapper;

    @Override
    public Location save(Location location) {
        LocationEntity entity = mapper.toEntity(location);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Location> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Location> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }
}
