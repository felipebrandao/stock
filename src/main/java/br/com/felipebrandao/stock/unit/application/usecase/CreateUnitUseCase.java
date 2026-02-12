package br.com.felipebrandao.stock.unit.application.usecase;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateUnitUseCase {

    private final UnitRepository unitRepository;

    public UUID execute(String name, String abbreviation) {
        String normalizedName = name == null ? null : name.trim();
        String normalizedAbbreviation = abbreviation == null ? null : abbreviation.trim().toUpperCase();

        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome da unidade é obrigatório");
        }

        if (normalizedAbbreviation == null || normalizedAbbreviation.isBlank()) {
            throw new BusinessException("Abreviação da unidade é obrigatória");
        }

        if (unitRepository.existsByName(normalizedName)) {
            throw new BusinessException("Já existe uma unidade com esse nome");
        }

        if (unitRepository.existsByAbbreviation(normalizedAbbreviation)) {
            throw new BusinessException("Já existe uma unidade com essa abreviação");
        }

        Unit created = unitRepository.save(Unit.create(normalizedName, normalizedAbbreviation));
        return created.getId();
    }
}
