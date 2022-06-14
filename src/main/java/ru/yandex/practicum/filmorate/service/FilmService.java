package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        if (userStorage.getById(userId) != null) {
            film.getLikes().add(userId);
        }
        log.trace("Запрос на добавление лайка от пользователя ID {} - DONE.", userId);
    }

    public void removeLike(int filmId, int userId) {
        Film film = filmStorage.getById(filmId);
        Set<Integer> likes = film.getLikes();
        if (userStorage.getById(userId) != null) {
            likes.remove(userId);
            log.trace("Запрос на удаление лайка от пользователя ID {} - DONE.", userId);
        }
    }

    public List<Film> getFavoriteFilms(int count) {
        List<Film> favoriteFilms = new ArrayList<>(filmStorage.get());
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
