package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();

    private Integer counter = 0;

    @Override
    public Film add(Film film) {
        createFilm(film);
        // Добавим фильм в фильмотеку.
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film update(Film film) {
        // Проверим наличие фильма в фильмотеке.
        final Integer filmId = film.getId();
        if (!isFilmExist(filmId)) {
            throw new NotFoundException("Фильм " + film + " отсутствует в фильмотеке");
        }
        films.put(filmId, film);
        return film;
    }

    @Override
    public Optional<Film> getById(Integer id) {
        if (!isFilmExist(id)) {
            return Optional.empty();
        }
        return Optional.of(films.get(id));
    }

    @Override
    public List<Film> getTop(Integer count) {
        if (films.isEmpty() || count < 1) {
            return new ArrayList<>();
        }

        List<Film> fulTop = new ArrayList<>(films.values());
        fulTop.sort(new Film.LikeComparator().reversed());
        return fulTop.subList(0, Integer.min(fulTop.size(), count));
    }

    @Override
    public void addLike(Film film, Integer userId) {
        Set<Integer> likes = film.getLikes();
        likes.add(userId);
    }

    @Override
    public void deleteLike(Film film, Integer userId) {
        Set<Integer> likes = film.getLikes();
        likes.remove(userId);
    }

    @Override
    public boolean isFilmExist(Integer filmId) {
        return films.containsKey(filmId);
    }

    private void createFilm(Film film) {
        film.setId(++counter);
    }

}
