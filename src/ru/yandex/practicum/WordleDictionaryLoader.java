package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader() {
        this(null);
    }

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary load(String fileName) throws IOException {
        List<String> allWords = new ArrayList<>();

        writeLog("Начинаем загрузку словаря: " + fileName);

        try (BufferedReader reader = new BufferedReader(
                new FileReader(fileName, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                allWords.add(line);
            }
        }

        List<String> gameWords = new ArrayList<>();

        for (String word : allWords) {
            String normalizedWord = WordleDictionary.normalizeWord(word);

            if (WordleDictionary.isCorrectWordFormat(normalizedWord)) {
                gameWords.add(normalizedWord);
            }
        }

        writeLog(
                "Словарь загружен. Подходящих слов: "
                        + gameWords.size()
        );

        return new WordleDictionary(gameWords, log);
    }

    private void writeLog(String message) {
        if (log != null) {
            log.println(message);
            log.flush();
        }
    }
}