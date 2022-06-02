package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;

import java.time.LocalDate;
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
    public Film addFilm(@RequestBody Film film) {
        validate(film);
        film.setId(++counterID);
        films.put(counterID, film);
        log.trace("Добавлен новый фильм: {}, ID {}", film.getName(), counterID);
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        validate(film);
        int filmID = film.getId();
        if (films.containsKey(filmID)) {
            films.put(filmID, film);
            log.trace("Обновлены данные фильма: {}, ID {}", film.getName(), counterID);
            return film;
        } else {
            throw new ValidationException("Указанного фильма нет в списке.");
        }
    }

    public void validate(Film film) {
        String description = film.getDescription();
        String name = film.getName();
        LocalDate releaseDate = film.getReleaseDate();
        int duration = film.getDuration();

        if (name.isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (description == null) {
            film.setDescription("");
        } else {
            if (description.length() > 200) {
                throw new ValidationException("Максимальная длина описания - 200 символов.");
            }
        }
        if (releaseDate.isBefore(LocalDate.of(1895,12,28))) {
            throw new ValidationException("Дата релиза - не раньше 28 декабря 1895 года.");
        }
        if (duration <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
