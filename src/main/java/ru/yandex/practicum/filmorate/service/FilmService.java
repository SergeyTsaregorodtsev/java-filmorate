package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll() {
        return filmStorage.getAll();
    }

    public Film getById(int filmId) {
        return filmStorage.getById(filmId);
    }

    public Film add(Film film) {
        return filmStorage.add(film);
    }

    public Film remove(int filmId) {
        Film deletedFilm = filmStorage.remove(filmId);
        log.trace("Запрос на удаление фильма ID {} - DONE.", filmId);
        return deletedFilm;
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Genre> getAllGenres() {
        return filmStorage.getAllGenres();
    }

    public Genre getGenre(int genreId) {
        return filmStorage.getGenre(genreId);
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }
    public Mpa getMpa(int mpaId) {
        return filmStorage.getMpa(mpaId);
    }

    public void addLike(int filmId, int userId) {
        userStorage.getById(userId);    // Проверяем, есть ли такой User, Film
        filmStorage.getById(filmId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        userStorage.getById(userId);    // Проверяем, есть ли такой User, Film
        filmStorage.getById(filmId);
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getFavorite(int count) {
        List<Film> favorite = filmStorage.getFavorite(count);
        log.trace("Запрос на получение {} фильмов с наибольшим количеством лайков - DONE.", count);
        return favorite;
    }
}
