package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

@Getter
@Setter
@ToString
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    @NotBlank
    @Size(max = 200)
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Positive
    private Integer duration;

    private Mpa mpa;

    private TreeSet<Genre> genres;
    private Set<Integer> likes = new HashSet<>();

    public Film() {}

    public Film(Integer id,
                String name,
                String description,
                LocalDate releaseDate,
                Integer duration,
                Mpa mpa,
                TreeSet<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.genres = genres;
        this.mpa = mpa;
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

    public static class LikeComparator implements Comparator<Film> {

        @Override
        public int compare(Film film1, Film film2) {
            return film1.likes.size() - film2.likes.size();
        }
    }

}
