package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public void execute(UUID id) {
        if (categoryRepository.findById(id).isEmpty()) {
            throw new NotFoundException("Categoria não encontrada");
        }
        categoryRepository.deleteById(id);
    }
}
