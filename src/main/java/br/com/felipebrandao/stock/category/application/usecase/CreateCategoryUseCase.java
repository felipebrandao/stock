package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.model.Category;
import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCategoryUseCase {

    private final CategoryRepository categoryRepository;

    public UUID execute(String name) {
        String normalizedName = name == null ? null : name.trim();

        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome da categoria é obrigatório");
        }

        if (categoryRepository.existsByName(normalizedName)) {
            throw new BusinessException("Já existe uma categoria com esse nome");
        }

        Category created = categoryRepository.save(Category.create(normalizedName));
        return created.getId();
    }
}
