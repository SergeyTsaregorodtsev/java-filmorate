package ru.yandex.practicum.filmorate.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private int counter;
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    @Override
    public List<Film> getFilms(){
        List<Film> filmSet = new ArrayList<>(films.values());
        log.trace("Запрос по списку фильмов - DONE. Количество фильмов в списке: {}", filmSet.size());
        return filmSet;
    }

    @Override
    public Film getFilmById(int filmId) {
        if (films.containsKey(filmId)) {
            log.trace("Запрос по фильму ID {} - DONE", filmId);
            return films.get(filmId);
        } else {
            throw new FilmNotFoundException(String.format("Фильм ID %d не найден", filmId));
        }
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(++counter);
        films.put(counter, film);
        log.trace("Добавлен новый фильм: {}, ID {}", film.getName(), counter);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int filmId = film.getId();
        if (films.containsKey(filmId)) {
            films.put(filmId, film);
            log.trace("Обновлены данные фильма: {}, ID {}", film.getName(), counter);
            return film;
        } else {
            throw new FilmNotFoundException(String.format("Фильм ID %d не найден", filmId));
        }
    }
}
