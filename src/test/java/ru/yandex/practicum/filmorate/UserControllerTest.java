package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.service.*;
import ru.yandex.practicum.filmorate.storage.*;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    JdbcTemplate jdbcTemplate = new JdbcTemplate();
    UserStorage userStorage = new UserDbStorage(jdbcTemplate);
    UserService userService = new UserService(userStorage);
    UserController controller = new UserController(userService);

    @Test
    void userIncorrectEmail(){
        User user = new User(1,"wrong-email", "login", "name", LocalDate.now(), null);
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(user);
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void userIncorrectLogin(){
        User user = new User(1, "box@email.ru", "log in", "name", LocalDate.now(), null);
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(user);
                    }
                });
        assertEquals("Логин не может быть пустым или содержать пробелы.", e.getMessage());
    }

    @Test
    void userEmptyName() {
        User user = new User(1,"box@email.ru", "login", "", LocalDate.now(), null);
        controller.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void userIncorrectBirthday(){
        User user = new User(1,"box@email.ru", "login", "name", LocalDate.now(), null);
        user.setBirthday(LocalDate.MAX);
        ValidationException e = assertThrows(ValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        controller.validate(user);
                    }
                });
        assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
    }
}
