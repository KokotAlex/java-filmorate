package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film add(Film film);

    List<Film> getAll();

    Optional<Film> update(Film film);

    Film getById(Integer id);

    List<Film> getTop(Integer count);

    Film addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    boolean isFilmExist(Integer filmId);

}
