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
@RequestMapping("/mpa")
public class MpaController {
    private final FilmStorage filmStorage;

    @Autowired
    public MpaController(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping
    public List<Mpa> getAllMpa(){
        log.trace("Получен GET-запрос на список пользователей.");
        return filmStorage.getAllMpa();
    }

    @GetMapping("/{mpaId}")
    public Mpa getMpa(@PathVariable int mpaId) {
        log.trace("Получен GET-запрос на рейтинг MPA ID {}.", mpaId);
        return filmStorage.getMpa(mpaId);
    }
}
