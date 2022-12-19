package ru.yandex.practicum.filmorate.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {

    private static final Logger log = LoggerFactory.getLogger(MpaService.class);

    private final MpaDbStorage storage;

    @Autowired
    public MpaService(MpaDbStorage storage) {
        this.storage = storage;
    }

    public List<Mpa> getAll() {
        log.debug("Начало получения списка возрастных рейтингов");

        List<Mpa> mpaRatings = storage.getAll();

        log.debug("Начало получения списка возрастных рейтингов");

        return mpaRatings;
    }

    public Mpa getById(Integer id) {
        log.debug("Начало получения возрастного рейтинга с id: {}", id);

        Mpa mpa = storage.getById(id).orElseThrow(() -> new NotFoundException("Mpa с id:" + id + " не найден."));

        log.debug("Начало получения возрастного рейтинга с id: {}", id);

        return mpa;
    }

}
