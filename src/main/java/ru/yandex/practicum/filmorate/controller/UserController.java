package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.model.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService,
                          @Qualifier("userDbStorage") UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @GetMapping
    public List<User> getUsers(){
        log.trace("Получен GET-запрос на список пользователей.");
        return userStorage.getAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        log.trace("Получен GET-запрос на пользователя ID {}.", userId);
        return userStorage.getById(userId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.trace("Получен POST-запрос на добавление пользователя {}.", user.getName());
        validate(user);
        return userStorage.add(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.trace("Получен PUT-запрос на обновление пользователя {}.", user.getName());
        validate(user);
        return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id,
                          @PathVariable int friendId) {
        log.trace("Получен PUT-запрос на добавление в друзья ID {} к ID {}.", friendId, id);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable int id,
                             @PathVariable int friendId) {
        log.trace("Получен DELETE-запрос на удаление из друзей ID {} у пользователя ID {}.", friendId, id);
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.trace("Получен GET-запрос на получение списка друзей ID {}.", id);
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id,
                                      @PathVariable int otherId) {
        log.trace("Получен GET-запрос на получение списка общих друзей ID {} и ID {}.", id, otherId);
        return userService.getCommonFriends(id, otherId);
    }

    public void validate(User user) {
        String name = user.getName();
        String login = user.getLogin();
        String email = user.getEmail();
        LocalDate birthday = user.getBirthday();

        if (name.isBlank()) {
            user.setName(login);
        }
        if (email.isBlank() || !email.contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (login.isBlank() || login.contains(" ")) {
            throw new ValidationException("Логин не может быть пустым или содержать пробелы.");
        }
        if (birthday != null && birthday.isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
