package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film add(Film film) {
        // Добавим фильм в базу фильмов.
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("FILMS").
                usingGeneratedKeyColumns("FILM_ID").
                usingColumns("FILM_NAME",
                        "DESCRIPTION",
                        "RELEASE_DATE",
                        "DURATION",
                        "MPA_ID");

        Map<String, Object> params = new HashMap<>();
        params.put("FILM_NAME", film.getName());
        params.put("DESCRIPTION", film.getDescription());
        params.put("RELEASE_DATE", film.getReleaseDate());
        params.put("DURATION", film.getDuration());
        if (film.getMpa() != null) {
            params.put("MPA_ID", film.getMpa().getId());
        }

        Integer filmId = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        film.setId(filmId);

        // Добавим жанр в таблицу жанров  фильмов.
        addFilmGenres(film);

        // Получим созданный в базе данных фильм.
        return getById(filmId).orElseThrow(() -> new NotFoundException("Film с id:" + filmId + " не найден."));
    }

    @Override
    public List<Film> getAll() {
        // Сформируем запрос выборки фильмов.
        final String FILM_QUERY = "SELECT FILMS.FILM_ID,\n" +
                "       FILMS.FILM_NAME,\n" +
                "       FILMS.DESCRIPTION,\n" +
                "       FILMS.RELEASE_DATE,\n" +
                "       FILMS.DURATION,\n" +
                "       FILMS.MPA_ID,\n" +
                "       MPA.MPA_NAME,\n" +
                "       MPA.DESCRIPTION\n" +
                "FROM FILMS\n" +
                "LEFT JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FILM_QUERY);
        // Сформируем запрос выборки жанров.
        final String GENRE_QUERY = "SELECT FILM_GENRE.FILM_ID,\n" +
                "       FILM_GENRE.GENRE_ID,\n" +
                "       GENRE.GENRE_NAME\n" +
                "FROM FILM_GENRE\n" +
                "LEFT JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.GENRE_ID";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_QUERY);
        // Сформируем запрос выборки лайков.
        final String LIKE_QUERY = "SELECT FILM_ID,\n" +
                "       USER_ID\n" +
                "FROM FILM_LIKES";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(LIKE_QUERY);

        // Сформируем фильмы по результатам запросов.
        return createFilmsFromRowSets(filmRows, genreRows, likeRows);
    }

    @Override
    public Film update(Film film) {
        Integer filmId = film.getId();
        // Обновим строку таблицы фильма.
        final String FILM_UPDATE = "UPDATE FILMS SET FILM_NAME = ?," +
                "   DESCRIPTION = ?," +
                "   RELEASE_DATE = ?," +
                "   DURATION = ?," +
                "   MPA_ID = ?\n" +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_UPDATE,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa() == null ? null : film.getMpa().getId(),
                filmId);
        // Обновим данные жанров фильма.
        deleteFilmGenres(filmId);
        addFilmGenres(film);
        // Обновим данные лайков фильма.
        deleteFilmLikes(filmId);
        addFilmLikes(film);

        // Получим обновленный фильм.
        return getById(filmId).orElseThrow(() -> new NotFoundException("Film с id:" + filmId + " не найден."));
    }

    @Override
    public Optional<Film> getById(Integer filmId) {
        if (isFilmNotExist(filmId)) {
            return Optional.empty();
        }

        // Сформируем запрос выборки фильмов.
        final String FILM_QUERY = "SELECT FILMS.FILM_ID,\n" +
                "       FILMS.FILM_NAME,\n" +
                "       FILMS.DESCRIPTION,\n" +
                "       FILMS.RELEASE_DATE,\n" +
                "       FILMS.DURATION,\n" +
                "       FILMS.MPA_ID,\n" +
                "       MPA.MPA_NAME,\n" +
                "       MPA.DESCRIPTION\n" +
                "FROM FILMS\n" +
                "LEFT JOIN MPA ON FILMS.MPA_ID = MPA.MPA_ID\n" +
                "WHERE FILM_ID = ?";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(FILM_QUERY, filmId);
        // Сформируем запрос выборки жанров.
        final String GENRE_QUERY = "SELECT FILM_GENRE.FILM_ID,\n" +
                "       FILM_GENRE.GENRE_ID,\n" +
                "       GENRE.GENRE_NAME\n" +
                "FROM FILM_GENRE\n" +
                "LEFT JOIN GENRE ON FILM_GENRE.GENRE_ID = GENRE.GENRE_ID\n" +
                "WHERE FILM_GENRE.FILM_ID = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(GENRE_QUERY, filmId);
        // Сформируем запрос выборки лайков.
        final String LIKE_QUERY = "SELECT FILM_ID,\n" +
                "       USER_ID\n" +
                "FROM FILM_LIKES\n" +
                "WHERE FILM_ID = ?";
        SqlRowSet likeRows = jdbcTemplate.queryForRowSet(LIKE_QUERY, filmId);

        // Сформируем фильмы по результатам запросов.
        List<Film> createdFilms = createFilmsFromRowSets(filmRows, genreRows, likeRows);

        return Optional.of(createdFilms.get(0));
    }

    @Override
    public List<Film> getTop(Integer count) {
        if (count < 1) {
            return new ArrayList<>();
        }

        // Сформируем запрос на выборку ТОП фильмов.
        final String SQL_QUERY = "SELECT FILMS.FILM_ID\n" +
                "FROM FILMS\n" +
                "LEFT JOIN FILM_LIKES ON FILMS.FILM_ID = FILM_LIKES.FILM_ID\n" +
                "GROUP BY FILMS.FILM_ID\n" +
                "ORDER BY COUNT(FILM_LIKES.USER_ID) DESC\n" +
                "LIMIT " + count;
        SqlRowSet filmsRows = jdbcTemplate.queryForRowSet(SQL_QUERY);
        // Обработаем запрос.
        List <Film> films = new ArrayList<>();
        while (filmsRows.next()) {
            Integer filmId = filmsRows.getInt("FILM_ID");
            Film film = getById(filmId).orElseThrow(() -> new NotFoundException("Film с id:" + filmId + " не найден."));
            films.add(film);
        }

        return films;
    }

    @Override
    public Film addLike(Integer filmId, Integer userId) {
        addFilmLikes(filmId, userId);
        return getById(filmId).orElseThrow(() -> new NotFoundException("Film с id:" + filmId + " не найден."));
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        deleteFilmLikes(filmId, userId);
    }

    @Override
    public boolean isFilmNotExist(Integer filmId) {
        // Сформируем запрос выборки фильма по id.
        final String SQL_QUERY = "SELECT FILM_ID\n" +
                "FROM FILMS\n" +
                "WHERE FILMS.FILM_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(SQL_QUERY, filmId);
        // обрабатываем результат выполнения запроса
        return !userRows.next();
    }

    private List<Film> createFilmsFromRowSets(SqlRowSet filmRows,
                                             SqlRowSet genreRows,
                                             SqlRowSet likeRows) {
        // Получим жанры фильмов.
        Map <Integer, TreeSet<Genre>> filmsGenre = createFilmsGenreFromRowSet(genreRows);
        // Получим лайки фильмов.
        Map <Integer, Set<Integer>> filmsLikes = createFilmsLikesFromRowSet(likeRows);
        // Создадим и заполним объекты фильмов.
        List<Film> films = new ArrayList<>();
        while (filmRows.next()) {
            // Сформируем объект mpa.
            int mpaId = filmRows.getInt("MPA_ID");
            Mpa filmMpa = null;
            if (mpaId > 0) {
                filmMpa = new Mpa(filmRows.getInt("MPA_ID"),
                        filmRows.getString("MPA_NAME"),
                        filmRows.getString("DESCRIPTION"));
            }
            // Сформируем объект фильма.
            Integer filmId = filmRows.getInt("FILM_ID");
            Film film = new Film(filmId,
                filmRows.getString("FILM_NAME"),
                filmRows.getString("DESCRIPTION"),
                Objects.requireNonNull(filmRows.getDate("RELEASE_DATE")).toLocalDate(),
                filmRows.getInt("DURATION"),
                filmMpa,
                filmsGenre.getOrDefault(filmId, new TreeSet<>()));

            if (filmsLikes.containsKey(filmId)) {
                film.setLikes(filmsLikes.get(filmId));
            }

            films.add(film);
        }

        return films;
    }

    private Map <Integer, TreeSet<Genre>> createFilmsGenreFromRowSet(SqlRowSet genreRows) {
        Map <Integer, TreeSet<Genre>> filmsGenre = new HashMap<>();
        while (genreRows.next()) {
            // Сформируем объект жанра.
            Genre filmGenre = new Genre(genreRows.getInt("GENRE_ID"),
                    genreRows.getString("GENRE_NAME"));
            // Получим идентификатор фильма который относится к текущему жанру.
            Integer filmId = genreRows.getInt("FILM_ID");

            // Добавим полученный жанр в Set жанров фильма.
            if (!filmsGenre.containsKey(filmId)) {
                filmsGenre.put(filmId, new TreeSet<>());
            }
            Set<Genre> genres = filmsGenre.get(filmId);
            genres.add(filmGenre);
        }

        return filmsGenre;
    }

    private Map <Integer, Set<Integer>> createFilmsLikesFromRowSet(SqlRowSet likeRows) {
        Map <Integer, Set<Integer>> filmsLikes = new HashMap<>();
        while (likeRows.next()) {
            Integer filmId = likeRows.getInt("FILM_ID");
            Integer userId = likeRows.getInt("USER_ID");
            if (!filmsLikes.containsKey(filmId)) {
                filmsLikes.put(filmId, new HashSet<>());
            }
            Set<Integer> likes = filmsLikes.get(filmId);
            likes.add(userId);
        }

        return filmsLikes;
    }

    private void deleteFilmGenres(Integer filmId) {
        final String FILM_GENRE_DELETE = "DELETE FROM FILM_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_GENRE_DELETE, filmId);
    }

    private void addFilmGenres(Film film) {
        if (film.getGenres() != null) {
            final String SQL_QUERY = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(SQL_QUERY, film.getId(), genre.getId());
            }
        }
    }

    private void deleteFilmLikes(Integer filmId) {
        final String FILM_LIKES_DELETE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ?";
        jdbcTemplate.update(FILM_LIKES_DELETE, filmId);
    }

    private void deleteFilmLikes(Integer filmId, Integer userId) {
        final String FILM_LIKES_DELETE = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(FILM_LIKES_DELETE, filmId, userId);
    }

    private void addFilmLikes(Film film) {
        if (film.getLikes() != null) {
            for (Integer userId : film.getLikes()) {
                addFilmLikes(film.getId(), userId);
            }
        }
    }

    private void addFilmLikes(Integer filmId, Integer userId) {
        final String SQL_QUERY = "INSERT INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?, ?)";
        jdbcTemplate.update(SQL_QUERY, filmId, userId);
    }

}
