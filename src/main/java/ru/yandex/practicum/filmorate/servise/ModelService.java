package ru.yandex.practicum.filmorate.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.util.List;

public interface ModelService<T> {

    Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    List<T> getAll();

    T add(T model);

    T update(T model);

    T getById(Integer id);

}
