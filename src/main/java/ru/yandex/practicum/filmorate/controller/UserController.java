package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.HashSet;

@RestController
@RequestMapping("/users")
public class UserController {

    private int counterID;
    private final HashMap<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public HashSet<User> getUsers(){
        HashSet<User> userSet = new HashSet<>(users.values());
        log.trace("Количество пользователей в текущий момент: {}", userSet.size());
        return userSet;
    }

    @PostMapping
    public User addUser(@Valid @RequestBody User user) throws User.UserValidationException {
        user.validate();
        user.setId(++counterID);
        users.put(counterID,user);
        log.trace("Добавлен новый пользователь: {}, ID {}", user.getName(), counterID);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws User.UserValidationException, User.UserExistException {
        user.validate();
        String login = user.getLogin();
        if (user.getName().isBlank()) {
            user.setName(login);
        }
        int userID = user.getId();
        if (users.containsKey(userID)) {
            users.put(userID, user);
            log.trace("Обновлены данные пользователя: {}, ID {}", user.getName(), counterID);
            return user;
        } else {
            throw new User.UserExistException("Указанный пользователь не существует.");
        }
    }
}
