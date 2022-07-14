package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
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

    public void addLike(int filmId, int userId) {
        userStorage.getById(userId);    // Проверяем, есть ли такой User
        filmStorage.getById(filmId);    // Проверяем, есть ли такой Film
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(int filmId, int userId) {
        userStorage.getById(userId);    // Проверяем, есть ли такой User
        filmStorage.getById(filmId);    // Проверяем, есть ли такой Film
        filmStorage.removeLike(filmId, userId);
    }

    public List<Film> getFavorite(int count) {
        List<Film> favorite = filmStorage.getFavorite(count);
        log.trace("Запрос на получение {} фильмов с наибольшим количеством лайков - DONE.", count);
        return favorite;
    }
}
