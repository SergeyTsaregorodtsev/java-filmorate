package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.trace("Запрос на добавление друга ID {} - DONE.", friendId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getById(userId);
        User friend = userStorage.getById(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.trace("Запрос на удаление друга ID {} - DONE.", friendId);
    }

    public Set<User> getFriends(int userId) {
        User user = userStorage.getById(userId);
        Set<Integer> friendsId = user.getFriends();
        Set<User> friends = new HashSet<>();
        for (Integer id : friendsId) {
            friends.add(userStorage.getById(id));
        }
        log.trace("Запрос на список друзей ID {} ({} друзей) - DONE.", userId, friends.size());
        return friends;
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        Set<User> commonFriends = new HashSet<>();
        User user = userStorage.getById(userId);
        User otherUser = userStorage.getById(otherId);
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                User commonFriend = userStorage.getById(id);
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }
}