package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.product.domain.model.Product;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public UUID execute(String name, UUID categoryId) {
        String normalizedName = name == null ? null : name.trim();

        if (normalizedName == null || normalizedName.isBlank()) {
            throw new BusinessException("Nome do produto é obrigatório");
        }
        if (categoryId == null) {
            throw new BusinessException("Categoria do produto é obrigatória");
        }
        if (categoryRepository.findById(categoryId).isEmpty()) {
            throw new BusinessException("Categoria não encontrada");
        }
        if (productRepository.existsByNameAndCategoryId(normalizedName, categoryId)) {
            throw new BusinessException("Já existe um produto com esse nome nesta categoria");
        }

        Product created = productRepository.save(Product.create(normalizedName, categoryId));
        return created.getId();
    }
}
