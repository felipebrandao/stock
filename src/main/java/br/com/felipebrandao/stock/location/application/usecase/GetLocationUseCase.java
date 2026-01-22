package br.com.felipebrandao.stock.location.application.usecase;

import br.com.felipebrandao.stock.location.domain.model.Location;
import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetLocationUseCase {

    private final LocationRepository repository;

    public Location execute(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Local não encontrado"));
    }
}
