package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {

    List<User> getUsers();

    User getUserById(int userId);

    User addUser(@Valid @RequestBody User user);

    User updateUser(@Valid @RequestBody User user);
}
