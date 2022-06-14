package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserControllerTest {
    UserStorage userStorage = new InMemoryUserStorage();
    UserService userService = new UserService(userStorage);
    UserController controller = new UserController(userService, userStorage);

    @Test
    void userIncorrectEmail(){
        User user = new User("wrong-email", "login", "name");
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
        User user = new User("box@email.ru", "log in", "name");
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
        User user = new User("box@email.ru", "login", "");
        controller.validate(user);
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void userIncorrectBirthday(){
        User user = new User("box@email.ru", "login", "name");
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
