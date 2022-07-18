package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmServiceTest {
    FilmService filmService;

    @Autowired
    public FilmServiceTest(FilmService filmService) {
        this.filmService = filmService;
    }

    @Test
    void filmWithWrongReleaseDate(){
        Film film = new Film(1,"Name", "desc.", LocalDate.of(1895,12,27),
                100, null,null);
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        filmService.validate(film);
                    }
                });
        assertEquals("Дата релиза - не раньше 28 декабря 1895 года.", e.getMessage());
    }

}
