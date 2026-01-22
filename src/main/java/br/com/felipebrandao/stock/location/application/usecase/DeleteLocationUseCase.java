package br.com.felipebrandao.stock.location.application.usecase;

import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteLocationUseCase {

    private final LocationRepository repository;

    public void execute(UUID id) {
        if (repository.findById(id).isEmpty()) {
            throw new NotFoundException("Local não encontrado");
        }
        repository.deleteById(id);
    }
}
