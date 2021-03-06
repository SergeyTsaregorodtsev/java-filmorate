package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage{
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAll() {
        final String sqlQuery = "SELECT * FROM FILMS";
        final List<Film> films = jdbcTemplate.query(sqlQuery, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                return createFilm(rs);
            }
        });
        return films;
    }

    @Override
    public Film getById(int id) {
        final String sqlQuery = "SELECT * FROM FILMS WHERE film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Film>() {
                @Override
                public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return createFilm(rs);
                }
            }, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Фильм с идентификатором {} не найден.", id);
            throw new FilmNotFoundException(String.format("Фильм ID %d не найден.", id));
        }
        return film;
    }

    @Override
    public Film add(Film film) {
        String sqlQuery = "INSERT INTO FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION,MPA_ID) values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
                stmt.setString(1, film.getName());
                stmt.setString(2, film.getDescription());
                final LocalDate releaseDate = film.getReleaseDate();
                if (releaseDate == null) {
                    stmt.setNull(3, Types.DATE);
                } else {
                    stmt.setDate(3, Date.valueOf(releaseDate));
                }
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5,film.getMpa().getId());
                return stmt;
            }
        }, keyHolder);
        int filmId = keyHolder.getKey().intValue();
        film.setId(filmId);

        if (film.getGenres() != null) {
            Set<Genre> genres = film.getGenres();
            String sqlQuery2 = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) values (?,?)";
            for (Genre genre: genres) {
                jdbcTemplate.update(sqlQuery2,filmId,genre.getId());
            }
            film.setGenres(applyGenres(film.getId()));
        }
        film.setMpa(applyMpa(film.getMpa().getId()));

        return film;
    }

    @Override
    public Film remove(int filmId) {
        Film film = getById(filmId);
        String[] sqlQuery = {
                "DELETE FROM film_likes WHERE FILM_ID = ?",     // Из таблицы FILM_LIKES
                "DELETE FROM film_genres WHERE FILM_ID = ?",    // Из таблицы FILM_GENRES
                "DELETE FROM films WHERE FILM_ID = ?",          // Из таблицы FILMS
        };
        for (String query : sqlQuery) {
            jdbcTemplate.update(query, filmId);
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery =   "UPDATE FILMS " +
                            "SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                            "WHERE FILM_ID = ?";
        if (jdbcTemplate.update(sqlQuery, film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()) != 1) {
            log.info("Фильм с идентификатором {} не найден.", film.getId());
            throw new FilmNotFoundException(String.format("Фильм ID %d не найден",  film.getId()));
        }

        updateGenres(film);
        film.setMpa(applyMpa(film.getMpa().getId()));
        if (film.getGenres() != null && film.getGenres().size() > 0) {
            film.setGenres(applyGenres(film.getId()));
        }
        return film;
    }

    @Override
    public List<Film> getFavorite(int count) {
        String sqlQuery =   "SELECT FILMS.* FROM FILMS " +
                            "LEFT JOIN FILM_LIKES on FILMS.FILM_ID = FILM_LIKES.FILM_ID " +
                            "GROUP BY FILMS.FILM_ID " +
                            "ORDER BY COUNT(USER_ID) DESC " +
                            "LIMIT ?";
        final List<Film> favorite = jdbcTemplate.query(sqlQuery, new RowMapper<Film>() {
            @Override
            public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                return createFilm(rs);
            }
        }, count);
        return favorite;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM MPA ORDER BY MPA_ID";
        final List<Mpa> mpa = jdbcTemplate.query(sqlQuery, new RowMapper<Mpa>() {
            @Override
            public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
            }
        });
        return mpa;
    }

    @Override
    public Mpa getMpa(int mpaId) {
        final String sqlQuery = "SELECT * FROM MPA WHERE MPA_ID = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Mpa>() {
                @Override
                public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Mpa(rs.getInt("mpa_id"), rs.getString("name"));
                }
            }, mpaId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Рейтинг MPA ID {} не найден.", mpaId);
            throw new InvalidParameterException("MPA ID");
        }
        return mpa;
    }

    public List<Genre> getAllGenres() {
        String sqlQuery = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Genre(rs.getInt("genre_id"), rs.getString("name"));
            }
        });
        return genres;
    }

    public Genre getGenre(int genreId) {
        final String sqlQuery = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Genre>() {
                @Override
                public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return new Genre(rs.getInt("genre_id"), rs.getString("name"));
                }
            }, genreId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Жанр ID {} не найден.", genreId);
            throw new InvalidParameterException("Жанр ID");
        }
        return genre;
    }

    public void addLike(int filmId, int userId) {
        String sqlQuery = "MERGE INTO FILM_LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        String sqlQuery = "DELETE FROM FILM_LIKES WHERE (FILM_ID, USER_ID) = (?,?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    private Film createFilm(ResultSet rs) throws SQLException {
        return new Film(rs.getInt("film_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getDate("release_date").toLocalDate(),
                rs.getInt("duration"),
                applyGenres(rs.getInt("film_id")),
                applyMpa(rs.getInt("mpa_id")));
    }

    private Mpa applyMpa(int mpaId) {
        String sqlQuery = "SELECT * FROM MPA WHERE mpa_id = ?";
        Mpa mpa;
        try {
            mpa = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<Mpa>() {
                @Override
                public Mpa mapRow(ResultSet rs, int rowNum) throws SQLException {
                    log.trace("Фильму присовен рейтинг MPA {} {}.", mpaId, rs.getString("name"));
                    return new Mpa(mpaId, rs.getString("name"));
                }
            }, mpaId);
        } catch (EmptyResultDataAccessException e) {
            log.info("Рейтинг MPA с идентификатором {} не найден.", mpaId);
            throw new InvalidParameterException("Рейтинг MPA");
        }
        return mpa;
    }

    private Set<Genre> applyGenres(int filmId) {
        String sqlQuery =   "SELECT FILM_GENRES.GENRE_ID, GENRES.NAME FROM FILM_GENRES " +
                            "LEFT OUTER JOIN GENRES ON FILM_GENRES.GENRE_ID = GENRES.GENRE_ID " +
                            "WHERE FILM_ID = ?";
        final List<Genre> genres = jdbcTemplate.query(sqlQuery, new RowMapper<Genre>() {
            @Override
            public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new Genre(rs.getInt("genre_id"), rs.getString("name"));
            }
        }, filmId);
        if (genres.size() == 0) {
            log.info("Жанры у фильма ID {} не указаны.", filmId);
        }
        return new HashSet<>(genres);
    }

    private void updateGenres (Film film) {
        String sqlQuery = "DELETE FROM FILM_GENRES WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
        if (film.getGenres() != null) {
            Set<Genre> genres = film.getGenres();
            for (Genre genre : genres) {
                sqlQuery = "MERGE INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?,?)";
                jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
            }
        }
    }

}