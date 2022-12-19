package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {

    private final FilmDbStorage storage;

    Film film1InMemory;
    Film film2InMemory;

    @BeforeEach
    void BeforeEach() {
        film1InMemory = new Film();
        film1InMemory.setName("Кошмар на улице Вязов");
        film1InMemory.setDescription("Очень страшный фильм");
        film1InMemory.setReleaseDate(LocalDate.of(1984, 11, 16));
        film1InMemory.setDuration(120);

        film2InMemory = new Film();
        film2InMemory.setName("Джонни Мнемоник");
        film2InMemory.setDescription("Клевое кино");
        film2InMemory.setReleaseDate(LocalDate.of(1995, 5, 26));
        film2InMemory.setDuration(120);
    }

    @Test
    void FilmDbStorageTest() {
        // Этап 1. Проверим, что запись фильма происходит без ошибок.
        Film dbFilm1 = assertDoesNotThrow(() -> storage.add(film1InMemory));
        assertEquals(1, storage.getAll().size());

        // Этап 2. Проверим, что созданный фильм в БД имеет поля, аналогичные полям фильма в памяти.
        assertEquals(film1InMemory.getName(), dbFilm1.getName());
        assertEquals(film1InMemory.getDescription(), dbFilm1.getDescription());
        assertEquals(film1InMemory.getReleaseDate(), dbFilm1.getReleaseDate());
        assertEquals(film1InMemory.getDuration(), dbFilm1.getDuration());

        // Этап 3. Сохраним в БД второй фильм и убедимся, что запрос по id возвращает верный.
        Film dbFilm2 = storage.add(film2InMemory);
        assertEquals(dbFilm2.getId(), (storage.getById(dbFilm2.getId()).get().getId()));

        // Этап 4. Проверим, что в базе сохранено 2 фильма.
        List<Film> dbFilms = storage.getAll();
        assertEquals(2, dbFilms.size());

        // Этап 5. Проверим, что данные фильма в базе обновляются.
        dbFilm2.setDuration(140);
        Film updatedDbFilm2 = storage.update(dbFilm2);
        assertEquals(140, updatedDbFilm2.getDuration());
    }

}