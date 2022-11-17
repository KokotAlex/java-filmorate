package ru.yandex.practicum.filmorate.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class FilmService implements ModelService<Film> {

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
        Optional<Film> filmOptional = filmStorage.update(film);
        if (filmOptional.isEmpty()) {
            throw new NotFoundException("Фильм " + film + " отсутствует в фильмотеке");
        }
        log.debug("Окончание обновления данных фильма {}", film);
        return filmOptional.get();
    }

    @Override
    public Film getById(Integer id) {
        log.debug("Начало получения фильма по id: {}", id);
        Film film = filmStorage.getById(id);
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
        if (!filmStorage.isFilmExist(filmId)) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        if (!userStorage.isUserExist(userId)) {
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
        if (!filmStorage.isFilmExist(filmId)) {
            throw new NotFoundException("Фильм с id: " + filmId + " не найден");
        }
        if (!userStorage.isUserExist(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId + " не найден");
        }
        // Удалим лайк.
        filmStorage.deleteLike(filmId, userId);
        log.debug("Окончание удаления лайка фильму с id {} пользователем с id {}", filmId, userId);
    }

}
