package ru.yandex.practicum;

public class GameFinishedException extends RuntimeException {

    public GameFinishedException(String message) {
        super(message);
    }
}