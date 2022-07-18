package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

public interface UserStorage {

    List<User> getAll();

    User getById(int userId);

    User add(User user);

    User remove(int userId);

    User update(User user);

    void addFriend(int userId, int friendId);

    List<User> getFriends(int userId);

    List<User> getCommonFriends(int userId, int otherId);

    void removeFriend(int userId, int friendId);
}
