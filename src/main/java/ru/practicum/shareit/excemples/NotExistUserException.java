package ru.practicum.shareit.excemples;

public class NotExistUserException extends RuntimeException{

    public NotExistUserException(String message) {
        super(message);
    }
}
