package ru.practicum.shareit.excemples;

public class NotExistItemException extends RuntimeException{
    public NotExistItemException(String message) {
        super(message);
    }
}
