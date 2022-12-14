package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.servise.GenreService;

import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenreController {

    protected static final Logger log = LoggerFactory.getLogger(GenreController.class);

    private final GenreService service;

    @Autowired
    public GenreController(GenreService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAll() {
        log.info("Обработка запроса на получение всех жанров.");

        List<Genre> genres = service.getAll();

        return ResponseEntity.ok(genres);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getById(@PathVariable Integer id) {
        log.info("Обработка запроса получения жанра с id {}", id);

        Genre genre = service.getById(id);

        return ResponseEntity.ok(genre);
    }

}
