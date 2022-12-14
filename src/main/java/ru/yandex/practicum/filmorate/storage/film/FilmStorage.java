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

    Film addLike(Integer filmId, Integer userId);

    void deleteLike(Integer filmId, Integer userId);

    boolean isFilmNotExist(Integer filmId);

}
