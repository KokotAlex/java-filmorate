package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserValidator {

    public static  void userValidate(User user) throws NullPointerException, ValidationException {
        emailValidate(user.getEmail());
        loginValidate(user.getLogin());
        birthdayValidate(user.getBirthday());
    }

    private static void emailValidate (String email) throws NullPointerException, ValidationException {
        if (email == null) {
            throw new NullPointerException("email не должен быть null");
        } else if (email.isBlank()) {
            throw new ValidationException("email должен быть заполнен");
        } else if (!email.contains("@")) {
            throw new ValidationException("email имеет некорректный формат.");
        }
    }

    private static void loginValidate (String login) throws NullPointerException, ValidationException {
        if (login == null) {
            throw new NullPointerException("login не должен быть null");
        } else if (login.isBlank()) {
            throw new ValidationException("login должен быть заполнен");
        }
    }

    private static void birthdayValidate (LocalDate birthday) throws ValidationException {
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть больше текущей даты.");
        }
    }


}
