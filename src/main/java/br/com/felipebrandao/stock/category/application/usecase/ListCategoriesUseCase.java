package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.model.Category;
import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListCategoriesUseCase {

    private final CategoryRepository categoryRepository;

    public List<Category> execute() {
        return categoryRepository.findAll();
    }

    public Page<Category> execute(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }
}
