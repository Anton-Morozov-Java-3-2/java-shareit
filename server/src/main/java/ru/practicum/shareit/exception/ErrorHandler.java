package ru.practicum.shareit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ErrorHandler {

    private ErrorResponse errorResponse;

    @ExceptionHandler({
            EmailAlreadyExistsException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictRequest(final Exception e) {
        return new ErrorResponse("Ошибка в данных: " + e.getMessage());
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            ItemAccessException.class,
            ItemNotFoundException.class,
            BookingAccessException.class,
            BookingNotFoundException.class,
            ItemRequestNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            ItemNotAvailableException.class,
            BookingDateNotValidException.class,
            InvalidParamException.class,
            ItemChangeStatusException.class,
            CommentNotAvailableException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequest(final Exception e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllError(final Throwable e) {
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }

    private static  class ErrorResponse {
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }


}
