package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms(){
        log.trace("Получен GET-запрос на список фильмов.");
        return filmService.getAll();
    }

    @GetMapping("/{filmId}")
    public Film getFilm(@PathVariable int filmId) {
        log.trace("Получен GET-запрос на фильм ID {}.", filmId);
        return filmService.getById(filmId);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.trace("Получен POST-запрос на добавление фильма {}.", film.getName());
        return filmService.add(film);
    }

    @DeleteMapping("/{filmId}")
    public void remove(@PathVariable int filmId) {
        log.trace("Получен DELETE-запрос на удаление фильма ID {}.", filmId);
        filmService.remove(filmId);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.trace("Получен PUT-запрос на обновление фильма {}.", film.getName());
        return filmService.update(film);
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
}
