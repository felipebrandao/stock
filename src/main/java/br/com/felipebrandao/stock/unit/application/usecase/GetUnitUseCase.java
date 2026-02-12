package br.com.felipebrandao.stock.unit.application.usecase;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUnitUseCase {

    private final UnitRepository unitRepository;

    public Unit execute(UUID id) {
        return unitRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Unidade não encontrada"));
    }
}
