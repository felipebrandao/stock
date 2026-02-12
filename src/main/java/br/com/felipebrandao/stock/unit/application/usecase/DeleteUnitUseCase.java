package br.com.felipebrandao.stock.unit.application.usecase;

import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteUnitUseCase {

    private final UnitRepository unitRepository;

    public void execute(UUID id) {
        if (unitRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Unidade não encontrada");
        }
        unitRepository.deleteById(id);
    }
}
