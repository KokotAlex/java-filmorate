package ru.yandex.practicum.filmorate.servise;

import java.util.List;

public interface ModelService<T> {

    List<T> getAll();

    T add(T model);

    T update(T model);

    T getById(Integer id);

}
