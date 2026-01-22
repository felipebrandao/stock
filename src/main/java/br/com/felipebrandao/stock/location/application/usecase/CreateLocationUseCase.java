package br.com.felipebrandao.stock.location.application.usecase;

import br.com.felipebrandao.stock.location.domain.model.Location;
import br.com.felipebrandao.stock.location.domain.repository.LocationRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateLocationUseCase {

    private final LocationRepository repository;

    public UUID execute(String name, String description) {
        String normalizedName = name == null ? null : name.trim();
        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome do local é obrigatório");
        }
        if (repository.existsByName(normalizedName)) {
            throw new BusinessException("Já existe um local com esse nome");
        }

        Location created = repository.save(Location.create(normalizedName, description));
        return created.getId();
    }
}
