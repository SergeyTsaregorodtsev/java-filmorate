package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private int counter;
    private final HashMap<Integer, User> users = new HashMap<>();

    public List<User> get(){
        List<User> userSet = new ArrayList<>(users.values());
        log.trace("Запрос по списку пользователей - DONE. Количество пользователей в списке: {}", userSet.size());
        return userSet;
    }

    @Override
    public User getById(int userId) {
        if (users.containsKey(userId)) {
            log.trace("Запрос по пользователю ID {} - DONE", userId);
            return users.get(userId);
        } else {
            throw new UserNotFoundException(String.format("Пользователь ID %d не найден", userId));
        }
    }

    @Override
    public User add(User user) {
        user.setId(++counter);
        users.put(counter,user);
        log.trace("Добавлен новый пользователь: {}, ID {}", user.getName(), counter);
        return user;
    }

    @Override
    public User remove(int userId) {
        log.trace("Удалён пользователь ID {}", userId);
        return users.remove(userId);
    }

    @Override
    public User update(User user) {
        int userId = user.getId();
        if (users.containsKey(userId)) {
            users.put(userId, user);
            log.trace("Обновлены данные пользователя: {}, ID {}", user.getName(), counter);
            return user;
        } else {
            throw new UserNotFoundException(String.format("Пользователь ID %d не найден", userId));
        }
    }
}
