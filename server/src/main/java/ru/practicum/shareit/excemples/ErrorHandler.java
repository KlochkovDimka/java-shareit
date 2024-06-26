package ru.practicum.shareit.excemples;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(NotExistUserEmailException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflict(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({NotExistUserException.class, NoSuchElementException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotExistItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse notBad(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(NotExistStatusName.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse internalServerError(RuntimeException e) {
        return new ErrorResponse(e.getMessage());
    }
}
