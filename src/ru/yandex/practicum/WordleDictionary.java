package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class WordleDictionary {

    private static final int WORD_LENGTH = 5;

    private final List<String> words;
    private final PrintWriter log;

    public WordleDictionary(List<String> words) {
        this(words, null);
    }

    public WordleDictionary(List<String> words, PrintWriter log) {
        this.words = new ArrayList<>(words);
        this.log = log;
    }

    public int size() {
        return words.size();
    }

    public boolean isEmpty() {
        return words.isEmpty();
    }

    public boolean contains(String word) {
        return words.contains(normalizeWord(word));
    }

    public List<String> getWords() {
        return Collections.unmodifiableList(words);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new IllegalStateException("Словарь пуст");
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(words.size());
        String word = words.get(randomIndex);

        writeLog("Выбрано случайное слово из словаря");

        return word;
    }

    public static String normalizeWord(String word) {
        if (word == null) {
            return "";
        }

        return word
                .trim()
                .toLowerCase()
                .replace('ё', 'е');
    }

    public static boolean isCorrectWordFormat(String word) {
        String normalizedWord = normalizeWord(word);

        return normalizedWord.matches("[а-я]{" + WORD_LENGTH + "}");
    }

    public static String compareWords(String answer, String enteredWord) {
        answer = normalizeWord(answer);
        enteredWord = normalizeWord(enteredWord);

        if (answer.length() != WORD_LENGTH
                || enteredWord.length() != WORD_LENGTH) {
            throw new IllegalArgumentException(
                    "Слова должны состоять из пяти букв"
            );
        }

        char[] result = new char[WORD_LENGTH];
        Arrays.fill(result, '-');

        boolean[] usedAnswerLetters = new boolean[WORD_LENGTH];
        boolean[] usedEnteredLetters = new boolean[WORD_LENGTH];

        // Сначала отмечаем точные совпадения.
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (answer.charAt(i) == enteredWord.charAt(i)) {
                result[i] = '+';
                usedAnswerLetters[i] = true;
                usedEnteredLetters[i] = true;
            }
        }

        // Затем ищем буквы, которые есть в слове,
        // но находятся на других местах.
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (usedEnteredLetters[i]) {
                continue;
            }

            char letter = enteredWord.charAt(i);

            for (int j = 0; j < WORD_LENGTH; j++) {
                if (!usedAnswerLetters[j]
                        && answer.charAt(j) == letter) {

                    result[i] = '^';
                    usedAnswerLetters[j] = true;
                    break;
                }
            }
        }

        return new String(result);
    }

    private void writeLog(String message) {
        if (log != null) {
            log.println(message);
            log.flush();
        }
    }
}