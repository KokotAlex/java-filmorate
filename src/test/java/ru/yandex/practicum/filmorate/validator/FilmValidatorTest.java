package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FilmValidatorTest {

    private static Film film;

    @BeforeEach
    void BeforeEach() {
        String name = "nisi eiusmod";
        String description = "adipisicing";
        LocalDate releaseDate = LocalDate.of(1967, 3, 25);
        Integer duration = 100;

        film = new Film(name, description, releaseDate, duration);
    }

    @Test
    @DisplayName("Проверка валидатора фильма.")
    void validateTest() {
        // Этап 1.
        // Проверим, что валидация фильма проходит.
        assertDoesNotThrow(() -> FilmValidator.validate(film));

        // Этап 2.
        // Проверим, что после установки некорректной даты релиза, валидация выдает ошибку.
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> FilmValidator.validate(film));
        assertEquals("Фильм не мог быть выпущен не раньше 28 декабря 1895.", validationException.getMessage());

    }

}