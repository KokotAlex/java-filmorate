package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    List<Film> getAll();

    Film update(Film film);

    Optional<Film> getById(Integer id);

    List<Film> getTop(Integer count);

    void addLike(Film film, Integer userId);

    void deleteLike(Film film, Integer userId);

    boolean isFilmExist(Integer filmId);

}
