package ru.yandex.practicum.filmorate.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public class FilmManagerInMemory implements FilmManager {

    private final Map<Integer, Film> films = new HashMap<>();
    private Integer counter = 0;
    private static final Logger log = LoggerFactory.getLogger(FilmManagerInMemory.class);

    @Override
    public Film addFilm(Film film) {
        log.debug("Начало добавления нового фильма {}", film);
        final Integer filmId = film.getId();
        // Проверим наличие фильма в фильмотеке.
        if (films.containsKey(filmId)) {
            throw new ValidationException("Фильм c id " + filmId + " уже есть в фильмотеке");
        }
        // Если для фильма не создан id, то создадим его.
        if (filmId == null) {
            createFilm(film);
        }
        // Добавим фильм в фильмотеку.
        films.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public List<Film> getFilm() {
        log.debug("Получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Начало обновления данных фильма {}", film);
        // Проверим наличие фильма в фильмотеке.
        final Integer filmId = film.getId();
        if (!films.containsKey(filmId)) {
            throw new ValidationException("Фильм c id " + filmId + " отсутствует в фильмотеке");
        } else {
            films.put(filmId, film);
            log.debug("Обновлен фильм {}", film);
            return film;
        }
    }

    private void createFilm(Film film) {
        film.setId(++counter);
    }

}
