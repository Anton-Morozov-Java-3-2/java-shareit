package ru.practicum.shareit.exception;

public class EmailAlreadyExistsException extends Exception{
    public EmailAlreadyExistsException(String message) {
        super(message);
    }

    public static String createMessage(String email) {
        return String.format("Email %s already exists.", email);
    }
}
