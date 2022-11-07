package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.FilmManagerInMemory;
import ru.yandex.practicum.filmorate.manager.ModelManagerAbs;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController extends ControllerAbs<Film> {

    private final ModelManagerAbs<Film> manager = new FilmManagerInMemory();

    @Override
    @GetMapping
    public ResponseEntity<List<Film>> getAll() {
        log.info("Обработка запроса на получение всех фильмов.");
        return ResponseEntity.ok(manager.get());
    }

    @Override
    @PostMapping
    public ResponseEntity<Film> add(@Valid  @RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма {}", film);
        FilmValidator.validate(film);
        return ResponseEntity.ok(manager.add(film));
    }

    @Override
    @PutMapping
    public ResponseEntity<Film> Update(@Valid @RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма {}", film);
        FilmValidator.validate(film);
        return ResponseEntity.ok(manager.update(film));
    }

}
