package br.com.felipebrandao.stock.shared.utils;

/**
 * Utilities to normalize Brazilian document numbers (CNPJ/CPF) and other numeric identifiers.
 */
public final class DocumentNormalizer {

    private DocumentNormalizer() {
    }

    public static String digitsOnly(String value) {
        if (value == null) return null;
        String digits = value.replaceAll("\\D+", "");
        return digits.isBlank() ? null : digits;
    }

    /**
     * Normalizes a CNPJ value to 14 digits (digits only).
     * Returns null when input is null/blank.
     */
    public static String normalizeCnpj(String value) {
        String digits = digitsOnly(value);
        if (digits == null) return null;
        return digits.length() <= 14 ? digits : digits.substring(0, 14);
    }

    /**
     * Normalizes a CEP to 8 digits (digits only).
     */
    public static String normalizeCep(String value) {
        String digits = digitsOnly(value);
        if (digits == null) return null;
        return digits.length() <= 8 ? digits : digits.substring(0, 8);
    }
}
