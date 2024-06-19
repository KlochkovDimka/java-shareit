package ru.practicum.shareit.excemples;

public class NotExistUserEmailException extends RuntimeException {
    public NotExistUserEmailException(String message) {
        super(message);
    }
}
