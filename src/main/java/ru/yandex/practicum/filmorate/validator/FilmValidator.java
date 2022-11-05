package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmValidator {
    public static  void filmValidate(Film film) throws NullPointerException, ValidationException {
        nameValidate(film.getName());
        descriptionValidate(film.getDescription());
        releaseDateValidate(film.getReleaseDate());
        durationValidate(film.getDuration());
    }

    private static void nameValidate (String name) throws NullPointerException, ValidationException {
        if (name == null) {
            throw new NullPointerException("название фильма не должно быть null");
        } else if (name.isBlank()) {
            throw new ValidationException("название фильма должно быть заполнено");
        }
    }

    private static void descriptionValidate (String description) throws ValidationException {
        if (description != null && description.length() > 200) {
            throw new ValidationException("Название фильма не должно превышать 200 символов.");
        }
    }

    private static void releaseDateValidate (LocalDate releaseDate) throws NullPointerException, ValidationException {
        if (releaseDate == null) {
            throw new NullPointerException("Не заполнена дата релиза.");
        } else if (releaseDate.isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Фильм не мог быть выпущен не раньше 28 декабря 1895.");
        }
    }

    private static void durationValidate (Integer duration) throws NullPointerException, ValidationException {
        if (duration == null) {
            throw new NullPointerException("Не заполнена продолжительность фильма.");
        } else if (duration <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть больше нуля.");
        }
    }

}
