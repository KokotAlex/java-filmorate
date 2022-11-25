package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Optional;

public interface FilmStorage extends Storage<Film> {

    @Override
    Film add(Film film);

    @Override
    List<Film> getAll();

    @Override
    Film update(Film film);

    @Override
    Optional<Film> getById(Integer id);

    List<Film> getTop(Integer count);

    void addLike(Film film, Integer userId);

    void deleteLike(Film film, Integer userId);

    boolean isFilmNotExist(Integer filmId);

}
