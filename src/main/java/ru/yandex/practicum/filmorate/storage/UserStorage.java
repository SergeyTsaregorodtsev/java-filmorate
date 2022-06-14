package ru.yandex.practicum.filmorate.storage;

import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {

    List<User> get();

    User getById(int userId);

    User add(@Valid @RequestBody User user);

    User remove(int userId);

    User update(@Valid @RequestBody User user);
}
