package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmValidatorTest {

    @Test
    @DisplayName("Проверка на валидность фильма.")
    void filmValidate() {
        String name = "nisi eiusmod";
        String description = "adipisicing";
        LocalDate releaseDate = LocalDate.of(1967, 3, 25);
        Integer duration = 100;

        // Проверим корректность валидации наименования.
        NullPointerException nullPointerException = assertThrows(
            NullPointerException.class,
            () -> new Film(null, description, releaseDate, duration));
        assertEquals("название фильма не должно быть null", nullPointerException.getMessage());
        ValidationException validationException = assertThrows(
                ValidationException.class,
                () -> new Film("    ", description, releaseDate, duration));
        assertEquals("название фильма должно быть заполнено", validationException.getMessage());

        // Проверим корректность валидации описания.
        validationException = assertThrows(
                ValidationException.class,
                () -> new Film(name,
                        "Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль." +
                                " Здесь они хотят разыскать господина Огюста Куглова, который задолжал им деньги," +
                                " а именно 20 миллионов. о Куглов," +
                                " который за время «своего отсутствия», стал кандидатом Коломбани.",
                        releaseDate,
                        duration));
        assertEquals("Название фильма не должно превышать 200 символов.", validationException.getMessage());

        // Проверим корректность валидации даты релиза.
        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> new Film(name, description, null, duration));
        assertEquals("Не заполнена дата релиза.", nullPointerException.getMessage());
        validationException = assertThrows(
                ValidationException.class,
                () -> new Film(name,
                        description,
                        LocalDate.of(1895, 12, 27),
                        duration));
        assertEquals("Фильм не мог быть выпущен не раньше 28 декабря 1895.", validationException.getMessage());

        // Проверим корректность валидации продолжительности фильма.
        nullPointerException = assertThrows(
                NullPointerException.class,
                () -> new Film(name, description, releaseDate, null));
        assertEquals("Не заполнена продолжительность фильма.", nullPointerException.getMessage());
        validationException = assertThrows(
                ValidationException.class,
                () -> new Film(name,
                        description,
                        releaseDate,
                        -100));
        assertEquals("Продолжительность фильма должна быть больше нуля.", validationException.getMessage());

    }

}