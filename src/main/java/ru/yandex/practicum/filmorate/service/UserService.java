package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        List<User> users = userStorage.getAll();
        log.trace("Запрос на получение списка пользователей - DONE.");
        return users;
    }

    public User getById(int userId) {
        User user = userStorage.getById(userId);
        log.trace("Запрос на получение пользователя ID {} - DONE.", userId);
        return user;
    }

    public User add(User user) {
        User newUser = userStorage.add(user);
        log.trace("Запрос на добавление пользователя ID {} - DONE.", newUser.getId());
        return newUser;
    }

    public User remove(int userId) {
        User deletedUser = userStorage.remove(userId);
        log.trace("Запрос на удаление пользователя ID {} - DONE.", userId);
        return deletedUser;
    }

    public User update(User user) {
        User updatedUser = userStorage.update(user);
        log.trace("Запрос на обновление пользователя ID {} - DONE.", updatedUser.getId());
        return updatedUser;
    }

    public void addFriend(int userId, int friendId) {
        userStorage.addFriend(userId, friendId);
        log.trace("Запрос на добавление друга ID {} - DONE.", friendId);
    }

    public void removeFriend(int userId, int friendId) {
        userStorage.removeFriend(userId, friendId);
        log.trace("Запрос на удаление друга ID {} у пользователя ID {} - DONE.", friendId, userId);
    }

    public List<User> getFriends(int userId) {
        List<User> friends = userStorage.getFriends(userId);
        log.trace("Запрос на список друзей ID {} ({} друзей) - DONE.", userId, friends.size());
        return friends;
    }

    public List<User> getCommonFriends(int userId, int otherId) {
        List<User> commonFriends = userStorage.getCommonFriends(userId, otherId);
        log.trace("Запрос на список общих друзей ID {} и ID {} - DONE.", userId, otherId);
        return commonFriends;
    }
}