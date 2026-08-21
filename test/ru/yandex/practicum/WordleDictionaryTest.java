package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryTest {

    @Test
    void shouldReturnDictionarySize() {
        WordleDictionary dictionary =
                new WordleDictionary(List.of("лампа", "арбуз", "ветер"));

        assertEquals(3, dictionary.size());
    }

    @Test
    void shouldReturnTrueWhenDictionaryIsEmpty() {
        WordleDictionary dictionary =
                new WordleDictionary(List.of());

        assertTrue(dictionary.isEmpty());
    }

    @Test
    void shouldFindWordInDictionary() {
        WordleDictionary dictionary =
                new WordleDictionary(List.of("лампа", "арбуз"));

        assertTrue(dictionary.contains("лампа"));
    }

    @Test
    void shouldNormalizeWordWhenSearching() {
        WordleDictionary dictionary =
                new WordleDictionary(List.of("лампа"));

        assertTrue(dictionary.contains("  ЛАМПА  "));
    }

    @Test
    void shouldNormalizeWord() {
        assertEquals("елка", WordleDictionary.normalizeWord("  ЁЛКА  "));
    }

    @Test
    void shouldReturnEmptyStringForNull() {
        assertEquals("", WordleDictionary.normalizeWord(null));
    }

    @Test
    void shouldReturnTrueForCorrectWordFormat() {
        assertTrue(WordleDictionary.isCorrectWordFormat("лампа"));
    }

    @Test
    void shouldReturnFalseForIncorrectWordFormat() {
        assertFalse(WordleDictionary.isCorrectWordFormat("кот"));
        assertFalse(WordleDictionary.isCorrectWordFormat("машина"));
        assertFalse(WordleDictionary.isCorrectWordFormat("кот12"));
    }

    @Test
    void shouldMarkAllLettersAsCorrect() {
        String result = WordleDictionary.compareWords("лампа", "лампа");

        assertEquals("+++++", result);
    }

    @Test
    void shouldMarkLettersInWrongPositions() {
        String result = WordleDictionary.compareWords("арбуз", "зубра");

        assertEquals("^^+^^", result);
    }

    @Test
    void shouldMarkMissingLetters() {
        String result = WordleDictionary.compareWords("лампа", "снеги");

        assertEquals("-----", result);
    }

    @Test
    void shouldThrowExceptionForWrongWordLength() {
        assertThrows(
                IllegalArgumentException.class,
                () -> WordleDictionary.compareWords("кот", "лампа")
        );
    }

    @Test
    void shouldReturnRandomWordFromDictionary() {
        List<String> words = List.of("лампа", "арбуз", "ветер");
        WordleDictionary dictionary = new WordleDictionary(words);

        String randomWord = dictionary.getRandomWord();

        assertTrue(words.contains(randomWord));
    }

    @Test
    void shouldThrowExceptionForEmptyDictionary() {
        WordleDictionary dictionary = new WordleDictionary(List.of());

        assertThrows(
                IllegalStateException.class,
                dictionary::getRandomWord
        );
    }
}