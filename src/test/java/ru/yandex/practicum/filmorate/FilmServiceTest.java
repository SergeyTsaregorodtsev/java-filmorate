package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FilmServiceTest {
    FilmStorage filmStorage = new InMemoryFilmStorage();
    UserStorage userStorage = new InMemoryUserStorage();
    FilmService filmService = new FilmService(filmStorage, userStorage);

    @Test
    void getFavoriteFilms(){
        List<Film> favoriteFilms;
        Film film1 = new Film("Name1", LocalDate.of(1995,12,27));
        filmStorage.add(film1);
        Film film2 = new Film("Name2", LocalDate.of(1995,12,27));
        filmStorage.add(film2);
        Film film3 = new Film("Name3", LocalDate.of(1995,12,27));
        filmStorage.add(film3);

        User user1 = new User("box1@email.ru", "login1", "name1");
        userStorage.add(user1);
        User user2 = new User("box2@email.ru", "login2", "name2");
        userStorage.add(user2);
        filmService.addLike(2,1);
        favoriteFilms = filmService.getFavoriteFilms(1);
        assertEquals("Name2", favoriteFilms.get(0).getName(),
                "Ошибка: неверная сортировка фильмов с наибольшим кол-вом лайков");

        filmService.addLike(3,1);
        filmService.addLike(3,2);
        filmService.removeLike(2,1);
        filmService.addLike(1,1);
        favoriteFilms = filmService.getFavoriteFilms(10);
        assertEquals(3, favoriteFilms.size(),
                "Ошибка: неверный размер списка фильмов с наибольшим кол-вом лайков");
        assertEquals("Name3", favoriteFilms.get(0).getName(),
                "Ошибка: неверная сортировка фильмов с наибольшим кол-вом лайков");
        assertEquals("Name1", favoriteFilms.get(1).getName(),
                "Ошибка: неверная сортировка фильмов с наибольшим кол-вом лайков");
    }
}
