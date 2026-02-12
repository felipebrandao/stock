package br.com.felipebrandao.stock.unit.application.usecase;

import br.com.felipebrandao.stock.shared.exception.BusinessException;
import br.com.felipebrandao.stock.unit.domain.repository.UnitRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CreateUnitUseCaseTest {

    @Test
    @DisplayName("Deve validar que não pode criar unidade com nome duplicado")
    void shouldThrowWhenDuplicateName() {
        UnitRepository unitRepository = mock(UnitRepository.class);
        when(unitRepository.existsByName("Quilograma")).thenReturn(true);

        CreateUnitUseCase useCase = new CreateUnitUseCase(unitRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Quilograma", "KG"));
        verify(unitRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar que não pode criar unidade com abreviação duplicada")
    void shouldThrowWhenDuplicateAbbreviation() {
        UnitRepository unitRepository = mock(UnitRepository.class);
        when(unitRepository.existsByName("Quilograma")).thenReturn(false);
        when(unitRepository.existsByAbbreviation("KG")).thenReturn(true);

        CreateUnitUseCase useCase = new CreateUnitUseCase(unitRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Quilograma", "KG"));
        verify(unitRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar que nome é obrigatório")
    void shouldThrowWhenNameIsNull() {
        UnitRepository unitRepository = mock(UnitRepository.class);
        CreateUnitUseCase useCase = new CreateUnitUseCase(unitRepository);

        assertThrows(BusinessException.class, () -> useCase.execute(null, "KG"));
        assertThrows(BusinessException.class, () -> useCase.execute("", "KG"));
        assertThrows(BusinessException.class, () -> useCase.execute("  ", "KG"));
        verify(unitRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve validar que abreviação é obrigatória")
    void shouldThrowWhenAbbreviationIsNull() {
        UnitRepository unitRepository = mock(UnitRepository.class);
        CreateUnitUseCase useCase = new CreateUnitUseCase(unitRepository);

        assertThrows(BusinessException.class, () -> useCase.execute("Quilograma", null));
        assertThrows(BusinessException.class, () -> useCase.execute("Quilograma", ""));
        assertThrows(BusinessException.class, () -> useCase.execute("Quilograma", "  "));
        verify(unitRepository, never()).save(any());
    }
}

