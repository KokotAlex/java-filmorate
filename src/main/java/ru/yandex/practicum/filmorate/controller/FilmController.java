package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManager;
import ru.yandex.practicum.filmorate.manager.FilmManagerInMemory;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmManager manager = new FilmManagerInMemory();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilm() {
        log.info("Обработка запроса на получение всех фильмов.");
        return manager.getFilm();
    }

    @PostMapping
    public Film postFilm(@RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма {}", film);
        return manager.addFilm(film);
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма {}", film);
        return manager.updateFilm(film);
    }

}
