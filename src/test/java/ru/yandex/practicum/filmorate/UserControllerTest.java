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

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    UserService service;
    UserController controller;
    MockMvc mvc;
    ObjectMapper mapper;

    @Autowired
    public UserControllerTest(UserService service, UserController controller, MockMvc mvc, ObjectMapper mapper) {
        this.service = service;
        this.controller = controller;
        this.mvc = mvc;
        this.mapper = mapper;
    }

    @DisplayName("Проверка на создание пользователя")
    @DirtiesContext
    @Test
    public void testAddUser () throws Exception {
        User user1 = new User (0,"1@mail.ru", "1", "name1", LocalDate.now(), null);
        postWithOkRequest (user1, "/users");
        List<User> users = service.getAll ();
        Assertions.assertEquals (1, users.size ());
    }

    //Отправка Post запроса с ожиданием кода 200
    private <T> void postWithOkRequest (T object, String path) throws Exception {
        mvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsBytes(object)))
                .andExpect(status().isOk())
                .andReturn();
    }
}
