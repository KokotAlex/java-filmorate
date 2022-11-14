package ru.yandex.practicum.filmorate.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class ModelManagerAbs<T> {

    protected static final Logger log = LoggerFactory.getLogger(FilmManagerInMemory.class);

    public abstract T add(T model);

    public abstract List<T> get();

    public abstract T update(T model);

}
