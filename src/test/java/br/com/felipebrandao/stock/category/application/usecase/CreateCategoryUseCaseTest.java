package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.shared.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateCategoryUseCaseTest {

    @Test
    @DisplayName("Deve validar que não pode criar categoria com nome duplicado")
    void shouldThrowWhenDuplicateName() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        when(categoryRepository.existsByName("Bebidas")).thenReturn(true);

        CreateCategoryUseCase useCase = new CreateCategoryUseCase(categoryRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Bebidas"));
        verify(categoryRepository, never()).save(any());
    }
}
