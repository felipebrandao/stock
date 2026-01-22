package br.com.felipebrandao.stock.shared.utils;

import java.text.Normalizer;

public class TextNormalizer {

    public static String normalize(String text) {
        if (text == null) {
            return null;
        }

        return Normalizer.normalize(text, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .trim()
                .replaceAll("[^a-z0-9\\s]", "")
                .replaceAll("\\s+", " ");
    }
}

