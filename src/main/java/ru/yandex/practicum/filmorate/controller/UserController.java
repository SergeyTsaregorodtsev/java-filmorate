package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        log.trace("Получен GET-запрос на список пользователей.");
        return userService.getAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable int userId) {
        log.trace("Получен GET-запрос на пользователя ID {}.", userId);
        return userService.getById(userId);
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.trace("Получен POST-запрос на добавление пользователя {}.", user.getName());
        return userService.add(user);
    }

    @DeleteMapping("/{userId}")
    public void remove(@PathVariable int userId) {
        log.trace("Получен DELETE-запрос на удаление пользователя ID {}.", userId);
        userService.remove(userId);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.trace("Получен PUT-запрос на обновление пользователя {}.", user.getName());
        return userService.update(user);
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
}
