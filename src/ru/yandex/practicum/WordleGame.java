package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

public class WordleGame {

    public static final int MAX_STEPS = 6;

    private String answer;
    private int steps;
    private WordleDictionary dictionary;

    private final List<String> enteredWords = new ArrayList<>();
    private final List<String> results = new ArrayList<>();
    private final Set<String> usedHints = new HashSet<>();

    public WordleGame(WordleDictionary dictionary) {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым");
        }

        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
        this.steps = 0;
    }

    // Этот конструктор пригодится для тестов:
    // можно заранее указать загаданное слово.
    public WordleGame(WordleDictionary dictionary, String answer) {
        if (dictionary == null || dictionary.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым");
        }

        String normalizedAnswer = WordleDictionary.normalizeWord(answer);

        if (!WordleDictionary.isCorrectWordFormat(normalizedAnswer)) {
            throw new InvalidWordException(
                    "Загаданное слово должно состоять из пяти русских букв"
            );
        }

        this.dictionary = dictionary;
        this.answer = normalizedAnswer;
        this.steps = 0;
    }

    public String makeMove(String word) {
        if (isFinished()) {
            throw new GameFinishedException("Игра уже завершена");
        }

        String normalizedWord = WordleDictionary.normalizeWord(word);

        validateWord(normalizedWord);

        String result = WordleDictionary.compareWords(
                answer,
                normalizedWord
        );

        enteredWords.add(normalizedWord);
        results.add(result);

        steps++;

        return result;
    }

    private void validateWord(String word) {
        if (!WordleDictionary.isCorrectWordFormat(word)) {
            throw new InvalidWordException(
                    "Введите слово из пяти русских букв"
            );
        }

        if (!dictionary.contains(word)) {
            throw new WordNotFoundInDictionary(
                    "Такого слова нет в словаре"
            );
        }
    }

    public String getHint() {
        List<String> suitableWords = new ArrayList<>();

        for (String candidate : dictionary.getWords()) {

            if (enteredWords.contains(candidate)
                    || usedHints.contains(candidate)) {
                continue;
            }

            if (isSuitableCandidate(candidate)) {
                suitableWords.add(candidate);
            }
        }

        if (suitableWords.isEmpty()) {
            throw new NoHintAvailableException(
                    "Подходящих слов для подсказки не осталось"
            );
        }

        int randomIndex = ThreadLocalRandom.current()
                .nextInt(suitableWords.size());

        String hint = suitableWords.get(randomIndex);

        usedHints.add(hint);

        return hint;
    }

    private boolean isSuitableCandidate(String candidate) {

        for (int i = 0; i < enteredWords.size(); i++) {

            String previousWord = enteredWords.get(i);
            String previousResult = results.get(i);

            String candidateResult =
                    WordleDictionary.compareWords(
                            candidate,
                            previousWord
                    );

            if (!candidateResult.equals(previousResult)) {
                return false;
            }
        }

        return true;
    }

    public boolean isWon() {
        if (enteredWords.isEmpty()) {
            return false;
        }

        String lastWord =
                enteredWords.get(enteredWords.size() - 1);

        return answer.equals(lastWord);
    }

    public boolean isFinished() {
        return isWon() || steps >= MAX_STEPS;
    }

    public int getSteps() {
        return steps;
    }

    public int getRemainingSteps() {
        return MAX_STEPS - steps;
    }

    public String getAnswer() {
        return answer;
    }

    public List<String> getEnteredWords() {
        return new ArrayList<>(enteredWords);
    }

    public List<String> getResults() {
        return new ArrayList<>(results);
    }
}