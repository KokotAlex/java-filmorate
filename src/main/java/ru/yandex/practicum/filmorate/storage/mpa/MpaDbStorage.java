package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.*;

@Component
public class MpaDbStorage {

    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Mpa> getAll() {
        final String QUERY = "SELECT MPA_ID,\n" +
                "       MPA_NAME,\n" +
                "       DESCRIPTION\n" +
                "FROM MPA";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(QUERY);
        List<Mpa> mpaRatings = new ArrayList<>();
        while (mpaRows.next()) {
            mpaRatings.add(new Mpa(mpaRows.getInt("MPA_ID"),
                    mpaRows.getString("MPA_NAME"),
                    mpaRows.getString("DESCRIPTION")));
        }

        return mpaRatings;
    }

    public Optional<Mpa> getById(Integer id) {
        final String QUERY = "SELECT MPA_NAME,\n" +
                "       DESCRIPTION\n" +
                "FROM MPA\n" +
                "WHERE MPA_ID = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(QUERY, id);
        if (mpaRows.next()) {
            return Optional.of(new Mpa(id,
                    mpaRows.getString("MPA_NAME"),
                    mpaRows.getString("DESCRIPTION")));
        } else {
            return Optional.empty();
        }
    }

}
