package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectParameterException;
import ru.yandex.practicum.filmorate.servise.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    protected static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        log.info("Обработка запроса на получение всех фильмов.");
        List<Film> films = service.getAll();
        return ResponseEntity.ok(films);
    }

    @PostMapping
    public ResponseEntity<Film> add(@Valid  @RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма {}", film);
        FilmValidator.validate(film);
        Film addedFilm = service.add(film);
        return ResponseEntity.ok(addedFilm);
    }

    @PutMapping
    public ResponseEntity<Film> Update(@Valid @RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма {}", film);
        FilmValidator.validate(film);
        Film updatedFilm = service.update(film);
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Film> getById(@PathVariable Integer id) {
        log.info("Обработка запроса получения фильма с id {}", id);
        Film film = service.getById(id);
        return ResponseEntity.ok(film);
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Film>> getTop(@RequestParam(defaultValue = "10") Integer count) {
        log.info("Обработка запроса получения {} самых популярных фильмов", count);
        if (count < 1) {
            throw new IncorrectParameterException("count");
        }
        List<Film> films = service.getTop(count);
        return ResponseEntity.ok(films);
    }

    @PutMapping("/{id}/like/{userId}")
    public ResponseEntity<Film> addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Обработка запроса добавления лайка фильму с id {} пользователем с id {} ", id, userId);
        Film film = service.addLike(id, userId);
        return ResponseEntity.ok(film);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable Integer id, @PathVariable Integer userId) {
        log.info("Обработка запроса удаления лайка фильму с id {} пользователем с id {} ", id, userId);
        service.deleteLike(id, userId);
    }

}
