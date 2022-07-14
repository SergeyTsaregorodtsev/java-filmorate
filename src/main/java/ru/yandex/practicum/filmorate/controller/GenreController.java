package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/genres")
public class GenreController {
    private final FilmStorage filmStorage;

    @Autowired
    public GenreController(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Genre> getAllGenres(){
        log.trace("Получен GET-запрос на список пользователей.");
        return filmStorage.getAllGenres();
    }

    @GetMapping("/{genreId}")
    public Genre getGenre(@PathVariable int genreId) {
        log.trace("Получен GET-запрос на рейтинг MPA ID {}.", genreId);
        return filmStorage.getGenre(genreId);
    }
}
