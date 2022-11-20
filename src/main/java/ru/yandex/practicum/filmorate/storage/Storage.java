package ru.yandex.practicum.filmorate.storage;

import java.util.List;
import java.util.Optional;

public interface Storage<T> {

    T add(T entity);

    List<T> getAll();

    T update(T entity);

    Optional<T> getById(Integer id);

}
