package br.com.felipebrandao.stock.unit.application.usecase;

import br.com.felipebrandao.stock.unit.domain.model.Unit;
import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUnitsUseCase {

    private final UnitRepository unitRepository;

    public List<Unit> execute() {
        return unitRepository.findAll();
    }

    public Page<Unit> execute(Pageable pageable) {
        return unitRepository.findAll(pageable);
    }
}
