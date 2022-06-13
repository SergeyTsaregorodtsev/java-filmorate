package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserStorage userStorage;
    private final static Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers(){
        log.trace("Запрос на получение списка пользователей отправлен в хранилище.");
        return userStorage.getUsers();
    }

    public User getUser(int userId) {
        log.trace("Запрос на получение пользователя {} отправлен в хранилище.", userId);
        return userStorage.getUserById(userId);
    }

    public User addUser(User user) {
        log.trace("Запрос на добавление пользователя {} отправлен в хранилище.", user.getName());
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        log.trace("Запрос на обновление пользователя {} отправлен в хранилище.", user.getName());
        return userStorage.updateUser(user);
    }

    public void addFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friends;
        friends = user.getFriends();
        friends.add(friendId);
        friends = friend.getFriends();
        friends.add(userId);
        log.trace("Запрос на добавление друга ID {} - DONE.", friendId);
    }

    public void removeFriend(int userId, int friendId) {
        User user = userStorage.getUserById(userId);
        User friend = userStorage.getUserById(friendId);
        Set<Integer> friends;
        friends = user.getFriends();
        friends.remove(friendId);
        friends = friend.getFriends();
        friends.remove(userId);
        log.trace("Запрос на удаление друга ID {} - DONE.", friendId);
    }

    public Set<User> getFriends(int userId) {
        User user = userStorage.getUserById(userId);
        Set<Integer> friendsId = user.getFriends();
        Set<User> friends = new HashSet<>();
        for (Integer id : friendsId) {
            friends.add(userStorage.getUserById(id));
        }
        log.trace("Запрос на список друзей ID {} ({} друзей) - DONE.", userId, friends.size());
        return friends;
    }

    public Set<User> getCommonFriends(int userId, int otherId) {
        Set<User> commonFriends = new HashSet<>();
        User user = userStorage.getUserById(userId);
        User otherUser = userStorage.getUserById(otherId);
        for (Integer id : user.getFriends()) {
            if (otherUser.getFriends().contains(id)) {
                User commonFriend = userStorage.getUserById(id);
                commonFriends.add(commonFriend);
            }
        }
        return commonFriends;
    }
}