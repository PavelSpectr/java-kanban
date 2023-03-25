package ru.yandex.practicum.exceptions;

import java.io.IOException;

public class ManagerLoadException extends RuntimeException {
    public ManagerLoadException(String message) {
        super(message);
    }
}
