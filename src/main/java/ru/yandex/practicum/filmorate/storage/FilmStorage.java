package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> getFilms();

    Film getFilmById(int filmId);

    Film addFilm(@RequestBody Film film);

    Film updateFilm(@RequestBody Film film);
}
