package ru.yandex.practicum.exceptions;

import java.io.IOException;

public class ManagerLoadException extends IOException {
    public ManagerLoadException(String message) {
        super(message);
    }
}
