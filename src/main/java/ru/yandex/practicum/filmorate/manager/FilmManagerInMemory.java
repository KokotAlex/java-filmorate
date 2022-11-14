package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

public class FilmManagerInMemory extends ModelManagerAbs<Film> {

    private final Map<Integer, Film> films = new HashMap<>();

    private Integer counter = 0;

    @Override
    public Film add(Film film) {
        log.debug("Начало добавления нового фильма {}", film);
        createFilm(film);
        // Добавим фильм в фильмотеку.
        films.put(film.getId(), film);
        log.debug("Добавлен фильм {}", film);
        return film;
    }

    @Override
    public List<Film> get() {
        log.debug("Получение списка фильмов");
        return new ArrayList<>(films.values());
    }

    public Film update(Film film) {
        log.debug("Начало обновления данных фильма {}", film);
        // Проверим наличие фильма в фильмотеке.
        final Integer filmId = film.getId();
        if (!films.containsKey(filmId)) {
            throw new NotFoundException("Фильм c id " + filmId + " отсутствует в фильмотеке");
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
