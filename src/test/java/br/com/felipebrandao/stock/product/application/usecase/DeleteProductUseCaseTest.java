package br.com.felipebrandao.stock.product.application.usecase;

import br.com.felipebrandao.stock.product.domain.repository.ProductRepository;
import br.com.felipebrandao.stock.shared.exception.NotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class DeleteProductUseCaseTest {

    @Test
    @DisplayName("Deve lançar NotFoundException quando o produto não existir")
    void shouldThrowWhenNotFound() {
        ProductRepository productRepository = mock(ProductRepository.class);
        UUID id = UUID.randomUUID();
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        DeleteProductUseCase useCase = new DeleteProductUseCase(productRepository);

        assertThrows(NotFoundException.class, () -> useCase.execute(id));
        verify(productRepository, never()).deleteById(any());
    }
}
