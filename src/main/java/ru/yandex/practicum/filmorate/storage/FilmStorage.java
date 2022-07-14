package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.*;
import java.util.List;

public interface FilmStorage {

    List<Film> getAll();

    Film getById(int filmId);

    Film add(Film film);

    Film remove(int filmId);

    Film update(Film film);

    List<Film> getFavorite(int count);

    List<Mpa> getAllMpa();

    Mpa getMpa(int mpaId);

    List<Genre> getAllGenres();

    Genre getGenre(int genreId);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);
}
