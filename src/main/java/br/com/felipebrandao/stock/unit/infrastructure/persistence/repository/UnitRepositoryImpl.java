package br.com.felipebrandao.stock.unit.infrastructure.persistence.repository;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import br.com.felipebrandao.stock.unit.infrastructure.persistence.entity.UnitEntity;
import br.com.felipebrandao.stock.unit.infrastructure.persistence.mapper.UnitMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class UnitRepositoryImpl implements UnitRepository {

    private final UnitJpaRepository jpaRepository;
    private final UnitMapper mapper;

    @Override
    public Unit save(Unit unit) {
        UnitEntity entity = mapper.toEntity(unit);
        jpaRepository.save(entity);
        return mapper.toDomain(entity);
    }

    @Override
    public Optional<Unit> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Unit> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<Unit> findAll(Pageable pageable) {
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

    @Override
    public boolean existsByAbbreviation(String abbreviation) {
        return jpaRepository.existsByAbbreviationIgnoreCase(abbreviation);
    }
}

