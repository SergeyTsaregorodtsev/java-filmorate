package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class FilmControllerTest {
    FilmController controller = new FilmController();

    @Test
    void filmWithEmptyName(){
        Film film = new Film("", LocalDate.of(1980,1,1));
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(film);
                    }
                });
        assertEquals("Название фильма не может быть пустым.", e.getMessage());
    }

    @Test
    void filmWithTooLongDesc(){
        Film film = new Film("Name", LocalDate.of(1980,1,1));
        film.setDescription("Пятеро друзей ( комик-группа «Шарло»), приезжают в город Бризуль. Здесь они хотят " +
                "разыскать господина Огюста Куглова, который задолжал им деньги, а именно 20 миллионов. о Куглов, " +
                "который за время «своего отсутствия», стал кандидатом Коломбани.");
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(film);
                    }
                });
        assertEquals("Максимальная длина описания - 200 символов.", e.getMessage());
    }

    @Test
    void filmWithWrongReleaseDate(){
        Film film = new Film("Name", LocalDate.of(1895,12,27));
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(film);
                    }
                });
        assertEquals("Дата релиза - не раньше 28 декабря 1895 года.", e.getMessage());
    }

    @Test
    void filmWithNegativeDuration(){
        Film film = new Film("Name", LocalDate.of(1895,12,28));
        film.setDuration(-1);
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(film);
                    }
                });
        assertEquals("Продолжительность фильма должна быть положительной.", e.getMessage());
    }
}