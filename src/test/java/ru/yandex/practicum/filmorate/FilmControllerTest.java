package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    FilmService service;
    FilmController controller;
    MockMvc mvc;
    ObjectMapper mapper;

    @Autowired
    public FilmControllerTest(FilmService service, FilmController controller, MockMvc mvc, ObjectMapper mapper) {
        this.service = service;
        this.controller = controller;
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("Проверка на создание фильма")
    @DirtiesContext
    @Test
    public void testAddFilm () throws Exception {
        Film film = new Film (1,"name1", "film1Desc.", LocalDate.now(), 100,null,null);
        film.setMpa(new Mpa(1,"Комедия"));
        postWithOkRequest (film, "/films");
        List<Film> films = service.getAll ();
        Assertions.assertEquals (1, films.size ());
    }

    //Отправка Post запроса с ожиданием кода 200
    private <T> void postWithOkRequest (T object, String path) throws Exception {
        mvc.perform(post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(object)))
                .andExpect(status().isOk())
                .andReturn();
    }
}
