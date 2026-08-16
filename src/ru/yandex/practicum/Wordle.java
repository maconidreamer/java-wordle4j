package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    private static final String DICTIONARY_FILE = "words_ru.txt";
    private static final String LOG_FILE = "wordle.log";

    public static void main(String[] args) {

        try (
                PrintWriter log = new PrintWriter(
                        new FileWriter(LOG_FILE, StandardCharsets.UTF_8, true)
                );
                Scanner scanner = new Scanner(System.in)
        ) {

            log.println("Запуск игры Wordle");
            log.flush();

            WordleDictionaryLoader loader =
                    new WordleDictionaryLoader(log);

            WordleDictionary dictionary =
                    loader.load(DICTIONARY_FILE);

            WordleGame game =
                    new WordleGame(dictionary);

            System.out.println("Игра Wordle");
            System.out.println("Угадайте существительное из пяти букв.");
            System.out.println();
            System.out.println("+ — буква стоит на правильном месте");
            System.out.println("^ — буква есть в слове, но стоит на другом месте");
            System.out.println("- — такой буквы в слове нет");
            System.out.println();
            System.out.println(
                    "Нажмите Enter без ввода слова, чтобы получить подсказку."
            );

            while (!game.isFinished()) {

                System.out.println();
                System.out.println(
                        "Осталось попыток: "
                                + game.getRemainingSteps()
                );

                System.out.print("> ");

                String input = scanner.nextLine();

                // Пустая строка — запрос подсказки.
                // Попытка при этом не расходуется.
                if (input.isBlank()) {
                    try {
                        String hint = game.getHint();

                        System.out.println(
                                "Подсказка: " + hint
                        );

                        log.println(
                                "Пользователь запросил подсказку: " + hint
                        );
                        log.flush();

                    } catch (NoHintAvailableException exception) {
                        System.out.println(exception.getMessage());

                        log.println(exception.getMessage());
                        log.flush();
                    }

                    continue;
                }

                try {
                    String result = game.makeMove(input);

                    System.out.println("> " + result);

                    log.println(
                            "Введено слово: "
                                    + WordleDictionary.normalizeWord(input)
                                    + ", результат: "
                                    + result
                    );
                    log.flush();

                } catch (InvalidWordException
                         | WordNotFoundInDictionary exception) {

                    // Некорректное слово не расходует попытку.
                    System.out.println(exception.getMessage());

                    log.println(
                            "Некорректный ввод: "
                                    + input
                                    + ". "
                                    + exception.getMessage()
                    );
                    log.flush();
                }
            }

            System.out.println();

            if (game.isWon()) {
                System.out.println("Вы угадали слово!");
                log.println("Игрок победил.");
            } else {
                System.out.println("Попытки закончились.");
                log.println("Игрок проиграл.");
            }

            System.out.println(
                    "Загаданное слово: "
                            + game.getAnswer()
            );

            log.println(
                    "Загаданное слово: "
                            + game.getAnswer()
            );

            log.println("Игра завершена.");
            log.flush();

        } catch (IOException exception) {

            System.out.println(
                    "Ошибка при работе с файлом: "
                            + exception.getMessage()
            );

        } catch (Exception exception) {

            System.out.println(
                    "Произошла ошибка: "
                            + exception.getMessage()
            );
        }
    }
}