package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.servise.MpaService;

import java.util.List;

@Controller
@RequestMapping("/mpa")
public class MpaController {

    protected static final Logger log = LoggerFactory.getLogger(MpaController.class);

    private final MpaService service;

    @Autowired
    public MpaController(MpaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Mpa>> getAll() {
        log.info("Обработка запроса на получение всех возростных рейтингов.");

        List<Mpa> mpaRatings = service.getAll();

        return ResponseEntity.ok(mpaRatings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mpa> getById(@PathVariable Integer id) {
        log.info("Обработка запроса получения возростного рейтинга с id {}", id);

        Mpa mpa = service.getById(id);

        return ResponseEntity.ok(mpa);
    }

}
