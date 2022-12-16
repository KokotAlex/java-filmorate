package ru.yandex.practicum.filmorate.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class FilmService implements ModelService<Film> {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @Override
    public List<Film> getAll() {
        log.debug("Начало получения списка фильмов");

        List<Film> films = filmStorage.getAll();

        log.debug("Окончание получения списка фильмов");

        return films;
    }

    @Override
    public Film add(Film film) {
        log.debug("Начало добавления нового фильма {}", film);

        film = filmStorage.add(film);

        log.debug("Окончание добавления нового фильма {}", film);

        return film;
    }

    @Override
    public Film update(Film film) {
        log.debug("Начало обновления данных фильма {}", film);

        // Проверим наличие фильма в фильмотеке.
        Integer filmId = film.getId();
        if (filmStorage.isFilmNotExist(filmId)) {
            throw new NotFoundException("Фильм " + film + " отсутствует в фильмотеке");
        }
        Film updatedFilm = filmStorage.update(film);

        log.debug("Окончание обновления данных фильма {}", film);

        return updatedFilm;
    }

    @Override
    public Film getById(Integer id) {
        log.debug("Начало получения фильма по id: {}", id);

        Film film = filmStorage.getById(id)
                .orElseThrow(() -> new NotFoundException("film с id:" + id + " не найден."));

        log.debug("Окончание получения фильма по id: {}", id);

        return film;
    }

    public List<Film> getTop(Integer count) {
        log.debug("Начало получения Топ {} фильмов по количеству лайков", count);

        List<Film> films = filmStorage.getTop(count);

        log.debug("Начало получения Топ {} фильмов по количеству лайков", count);

        return films;
    }

    public Film addLike(Integer filmId, Integer userId) {
        log.debug("Начало добавления лайка фильму с id {} пользователем с id {}", filmId, userId);

        // Проверим существование пользователя и фильма с такими id.
        if (filmStorage.isFilmNotExist(filmId)) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        if (userStorage.isUserNotExist(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }

        // Добавим лайк.
        Film film = filmStorage.addLike(filmId, userId);

        log.debug("Окончание добавления лайка фильму с id {} пользователем с id {}", filmId, userId);

        return film;
    }

    public void deleteLike(Integer filmId, Integer userId) {
        log.debug("Начало удаления лайка фильму с id {} пользователем с id {}", filmId, userId);

        // Проверим существование пользователя и фильма с такими id.
        if (filmStorage.isFilmNotExist(filmId)) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        if (userStorage.isUserNotExist(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }

        // Удалим лайк.
        filmStorage.deleteLike(filmId, userId);

        log.debug("Окончание удаления лайка фильму с id {} пользователем с id {}", filmId, userId);
    }

}
