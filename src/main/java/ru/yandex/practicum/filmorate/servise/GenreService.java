package ru.yandex.practicum.filmorate.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
public class GenreService {

    private static final Logger log = LoggerFactory.getLogger(GenreService.class);

    private final GenreDbStorage storage;

    @Autowired
    public GenreService(GenreDbStorage storage) {
        this.storage = storage;
    }

    public List<Genre> getAll() {
        log.debug("Начало получения списка жанров");

        List<Genre> genres = storage.getAll();

        log.debug("Начало получения списка жанров");

        return genres;
    }

    public Genre getById(Integer id) {
        log.debug("Начало получения жанра с id: {}", id);

        Genre genre = storage.getById(id).orElseThrow(() -> new NotFoundException("Genre с id:" + id + " не найден."));

        log.debug("Начало получения жанра с id: {}", id);

        return genre;
    }

}
