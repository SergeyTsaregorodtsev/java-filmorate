package ru.yandex.practicum.filmorate.exception;

public class InvalidParameterException extends RuntimeException{
    private final String parameter;

    public InvalidParameterException(String parameter) {
        this.parameter = parameter;
    }

    public String getParameter() {
        return parameter;
    }
}
