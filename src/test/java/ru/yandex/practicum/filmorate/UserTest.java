package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.io.IOException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class UserTest {

    @Test
    void userIncorrectEmail(){
        User user = new User("wrong-email", "login", "name");
        User.UserValidationException e = assertThrows(User.UserValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        user.validate();
                    }
                });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
    }

    @Test
    void userIncorrectLogin(){
        User user = new User("box@email.ru", "log in", "name");
        User.UserValidationException e = assertThrows(User.UserValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        user.validate();
                    }
                });
        assertEquals("Логин не может быть пустым или содержать пробелы.", e.getMessage());
    }

    @Test
    void userEmptyName() {
        User user = new User("box@email.ru", "login", "");
        user.validate();
        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void userIncorrectBirthday(){
        User user = new User("box@email.ru", "login", "name");
        user.setBirthday(LocalDate.MAX);
        User.UserValidationException e = assertThrows(User.UserValidationException.class,
                new Executable() {
                    @Override
                    public void execute() throws IOException, InterruptedException {
                        user.validate();
                    }
                });
        assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
    }
}