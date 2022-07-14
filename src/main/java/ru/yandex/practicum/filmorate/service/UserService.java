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