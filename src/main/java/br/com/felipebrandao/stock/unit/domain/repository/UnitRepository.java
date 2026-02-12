package br.com.felipebrandao.stock.unit.domain.repository;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnitRepository {

    Unit save(Unit unit);

    Optional<Unit> findById(UUID id);

    List<Unit> findAll();

    Page<Unit> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsByName(String name);

    boolean existsByAbbreviation(String abbreviation);
}

