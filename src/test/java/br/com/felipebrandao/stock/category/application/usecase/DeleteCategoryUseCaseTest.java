package br.com.felipebrandao.stock.category.application.usecase;

import br.com.felipebrandao.stock.category.domain.repository.CategoryRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteCategoryUseCaseTest {

    @Test
    @DisplayName("Deve lançar NotFoundException quando a categoria não existir")
    void shouldThrowWhenNotFound() {
        CategoryRepository categoryRepository = mock(CategoryRepository.class);
        UUID id = UUID.randomUUID();
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        DeleteCategoryUseCase useCase = new DeleteCategoryUseCase(categoryRepository);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(categoryRepository, never()).deleteById(any());
    }
}
