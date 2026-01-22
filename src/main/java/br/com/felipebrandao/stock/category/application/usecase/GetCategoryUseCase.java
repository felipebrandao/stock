package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.model.Category;
import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public Category execute(UUID id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Categoria não encontrada"));
    }
}
