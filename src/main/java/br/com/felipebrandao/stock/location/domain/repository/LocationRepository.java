package br.com.felipebrandao.stock.location.domain.repository;

import br.com.felipebrandao.stock.location.domain.model.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface LocationRepository {

    Location save(Location location);

    Optional<Location> findById(UUID id);

    Page<Location> findAll(Pageable pageable);

    void deleteById(UUID id);

    boolean existsByName(String name);
}
