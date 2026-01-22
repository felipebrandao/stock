package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateProductUseCaseTest {

    @Test
    @DisplayName("Deve lançar BusinessException quando a categoria não existir")
    void shouldThrowWhenCategoryDoesNotExist() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        CreateProductUseCase useCase = new CreateProductUseCase(productRepository, categoryRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Arroz", categoryId));
        verify(productRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar BusinessException quando já existir produto com mesmo nome na categoria")
    void shouldThrowWhenDuplicateInCategory() {
        ProductRepository productRepository = mock(ProductRepository.class);
        CategoryRepository categoryRepository = mock(CategoryRepository.class);

        UUID categoryId = UUID.randomUUID();
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(mock(br.com.felipebrandao.stock.category.domain.model.Category.class)));
        when(productRepository.existsByNameAndCategoryId("Arroz", categoryId)).thenReturn(true);

        CreateProductUseCase useCase = new CreateProductUseCase(productRepository, categoryRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Arroz", categoryId));
        verify(productRepository, never()).save(any());
    }
}
