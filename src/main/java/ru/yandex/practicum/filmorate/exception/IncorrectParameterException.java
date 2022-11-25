package ru.yandex.practicum.filmorate.exception;

public class IncorrectParameterException extends RuntimeException {

    public String parameter;

    public IncorrectParameterException(String parameter) {
        this.parameter = parameter;
    }

    @Override
    public String getMessage() {
        return "Передано некорректное значение параметра " + parameter;
    }

}
