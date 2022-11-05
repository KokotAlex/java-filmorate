package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
public class Film {
    private Integer id;
    private final String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private static final Logger log = LoggerFactory.getLogger(Film.class);

    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        FilmValidator.filmValidate(this);
        log.info("Создан фильм: {}", this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
