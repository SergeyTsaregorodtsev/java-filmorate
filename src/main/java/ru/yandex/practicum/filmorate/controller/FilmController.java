package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController (FilmService filmService,
                           @Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Film> getFilms(){
        log.trace("Получен GET-запрос на список фильмов.");
        return filmStorage.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        log.trace("Получен GET-запрос на фильм ID {}.", filmId);
        return filmStorage.getById(filmId);
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.trace("Получен POST-запрос на добавление фильма {}.", film.getName());
        validate(film);
        return filmStorage.add(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.trace("Получен PUT-запрос на обновление фильма {}.", film.getName());
        validate(film);
        return filmStorage.update(film);
    }

    @PutMapping("{filmId}/like/{userId}")
    public void addLike(@PathVariable int filmId,
                        @PathVariable int userId) {
        log.trace("Получен PUT-запрос на добавления лайка фильму ID {} от пользователя ID {}.", filmId, userId);
        filmService.addLike(filmId, userId);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public void removeLike(@PathVariable int filmId,
                        @PathVariable int userId) {
        log.trace("Получен DELETE-запрос на удаление лайка фильму ID {} от пользователя ID {}.", filmId, userId);
        filmService.removeLike(filmId, userId);
    }

    @GetMapping("popular")
    public List<Film> getFavorite(@RequestParam(value = "count", defaultValue = "10", required = false) int count){
        log.trace("Получен GET-запрос на получение первых {} фильмов по количеству лайков.", count);
        return filmService.getFavorite(count);
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
