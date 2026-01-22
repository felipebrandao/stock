package br.com.felipebrandao.stock.shared.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DocumentNormalizerTest {

    @Test
    @DisplayName("Deve normalizar CNPJ removendo máscara e mantendo apenas dígitos")
    void normalizeCnpj_shouldKeepDigitsAndRemoveMask() {
        assertEquals("00776574120488", DocumentNormalizer.normalizeCnpj("00.776.574/1204-88"));
    }

    @Test
    @DisplayName("Deve retornar null ao normalizar CNPJ quando o valor é vazio ou não possui dígitos")
    void normalizeCnpj_shouldReturnNullForBlankOrNoDigits() {
        assertNull(DocumentNormalizer.normalizeCnpj(null));
        assertNull(DocumentNormalizer.normalizeCnpj(""));
        assertNull(DocumentNormalizer.normalizeCnpj("  "));
        assertNull(DocumentNormalizer.normalizeCnpj("CNPJ"));
    }

    @Test
    @DisplayName("Deve truncar CNPJ para 14 dígitos quando vier maior")
    void normalizeCnpj_shouldTruncateTo14DigitsWhenLonger() {
        assertEquals("12345678901234", DocumentNormalizer.normalizeCnpj("123456789012345678"));
    }

    @Test
    @DisplayName("Deve normalizar CEP removendo hífen e mantendo apenas dígitos")
    void normalizeCep_shouldKeepDigitsAndRemoveDash() {
        assertEquals("42840310", DocumentNormalizer.normalizeCep("42840-310"));
    }
}
