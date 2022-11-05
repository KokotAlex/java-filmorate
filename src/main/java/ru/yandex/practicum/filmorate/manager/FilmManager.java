package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmManager {

    Film addFilm(Film film);

    List<Film> getFilm();

    Film updateFilm(Film film);

}
