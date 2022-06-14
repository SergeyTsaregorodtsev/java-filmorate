package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    List<Film> get();

    Film getById(int filmId);

    Film add(@RequestBody Film film);

    Film remove(int filmId);

    Film update(@RequestBody Film film);
}
