package br.com.felipebrandao.stock.location.application.usecase;

import br.com.felipebrandao.stock.location.domain.model.Location;
import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ListLocationsUseCase {

    private final LocationRepository repository;

    public Page<Location> execute(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
