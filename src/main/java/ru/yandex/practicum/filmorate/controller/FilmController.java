package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;

import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/films")
public class FilmController {

    private int counterID;
    private final HashMap<Integer,Film> films = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public HashSet<Film> getFilms(){
        HashSet<Film> filmSet = new HashSet<>(films.values());
        log.trace("Количество фильмов в списке: {}", filmSet.size());
        return filmSet;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) throws Film.FilmValidationException {
        film.validate();
        film.setId(++counterID);
        films.put(counterID, film);
        log.trace("Добавлен новый фильм: {}, ID {}", film.getName(), counterID);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws Film.FilmValidationException, Film.FilmExistException {
        film.validate();
        int filmID = film.getId();
        if (films.containsKey(filmID)) {
            films.put(filmID, film);
            log.trace("Обновлены данные фильма: {}, ID {}", film.getName(), counterID);
            return film;
        } else {
            throw new Film.FilmExistException("Указанного фильма нет в списке.");
        }
    }
}
