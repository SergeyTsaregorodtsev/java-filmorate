package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final static Logger log = LoggerFactory.getLogger(FilmService.class);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        log.trace("Запрос на получение списка фильмов отправлен в хранилище.");
        return filmStorage.getFilms();
    }

    public Film getFilm(int filmId) {
        log.trace("Запрос на получение фильма ID {} отправлен в хранилище.", filmId);
        return filmStorage.getFilmById(filmId);
    }

    public Film addFilm(Film film) {
        log.trace("Запрос на добавление фильма {} отправлен в хранилище.", film.getName());
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        log.trace("Запрос на обновление фильма {} отправлен в хранилище.", film.getName());
        return filmStorage.updateFilm(film);
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        if (userStorage.getUserById(userId) != null) {
            film.getLikes().add(userId);
        }
        log.trace("Запрос на добавление лайка от пользователя ID {} - DONE.", userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getFilmById(filmId);
        Set<Integer> likes = film.getLikes();
        if (userStorage.getUserById(userId) != null) {
            likes.remove(userId);
            log.trace("Запрос на удаление лайка от пользователя ID {} - DONE.", userId);
        }
    }

    public List<Film> getFavoriteFilms(int count) {
        List<Film> favoriteFilms = new ArrayList<>(filmStorage.getFilms());
        if (count >= favoriteFilms.size()) {
            count = favoriteFilms.size();
        }
        Comparator<Film> filmComparator = new Comparator<>() {
            @Override
            public int compare(Film film1, Film film2) {
                int film1Likes = film1.getLikes().size();
                int film2Likes = film2.getLikes().size();
                return film2Likes - film1Likes;
            }
        };
        favoriteFilms.sort(filmComparator);
        log.trace("Запрос на получение {} фильмов с наибольшим количеством лайков - DONE.", count);
        return favoriteFilms.subList(0, count);
    }
}
