package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        final String sqlQuery = "SELECT * FROM USERS";
        final List<User> users = jdbcTemplate.query(sqlQuery, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                return createUser(rs);
            }
        });
        return users;
    }

    @Override
    public User getById(int id) {
        final String sqlQuery = "SELECT * FROM USERS WHERE user_id = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlQuery, new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    return createUser(rs);
                }
            }, id);
        } catch (EmptyResultDataAccessException e) {
            log.info("Пользователь с идентификатором {} не найден.", id);
            throw new UserNotFoundException(String.format("Пользователь ID %d не найден.", id));
        }
        return user;
    }

    @Override
    public User add(User user) {
        String sqlQuery = "INSERT INTO USERS (EMAIL, LOGIN, NAME, BIRTHDAY) values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
                stmt.setString(1, user.getEmail());
                stmt.setString(2, user.getLogin());
                stmt.setString(3, user.getName());
                final LocalDate birthday = user.getBirthday();
                if (birthday == null) {
                    stmt.setNull(4, Types.DATE);
                } else {
                    stmt.setDate(4, Date.valueOf(birthday));
                }
                return stmt;
            }
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User remove(int userId) {
        User user = getById(userId);
        String[] sqlQuery = {
                "DELETE FROM film_likes WHERE USER_ID = ?",     // Из таблицы FILM_LIKES
                "DELETE FROM friends WHERE USER_ID = ?",        // Из таблицы FRIENDS
                "DELETE FROM friends WHERE FRIEND_ID = ?",      // Из таблицы FRIENDS
                "DELETE FROM users WHERE USER_ID = ?",          // Из таблицы USERS
                };
        for (String query : sqlQuery) {
            jdbcTemplate.update(query, userId);
        }
        return user;
    }

    @Override
    public User update(User user) {
        String sqlQuery = "UPDATE users SET EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? WHERE USER_ID = ?";
        if (jdbcTemplate.update(sqlQuery, user.getEmail(),
                            user.getLogin(),
                            user.getName(),
                            user.getBirthday(),
                            user.getId()) != 1) {
                    log.info("Пользователь с идентификатором {} не найден.", user.getId());
                    throw new UserNotFoundException(String.format("Пользователь ID %d не найден",  user.getId()));
                    }
        return user;
    }

    @Override
    public void addFriend(int userId, int friendId) {
        getById(friendId);  // Проверяем, существует ли такой User
        String sqlQuery;

        // Были ли ранее запросы надружбу от friendId
        sqlQuery = "SELECT FRIEND_STATUS_ID FROM FRIENDS WHERE (USER_ID, FRIEND_ID) = (?,?)";
        List<Integer> status = jdbcTemplate.query(sqlQuery, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("friend_status_id");
            }
        }, friendId, userId);

        // Записей о дружбе нет (запросов не было), указываем статус UNCONFIRMED
        if (status.size() == 0) {
            sqlQuery = "INSERT INTO FRIENDS(USER_ID, FRIEND_ID, FRIEND_STATUS_ID) values (?, ?, ?)";
            jdbcTemplate.update(sqlQuery, userId, friendId, 1);
            log.info("Пользователю ID {} добавлен друг ID {} (UNCONFIRMED)", userId, friendId);

        // Ранее был запрос на дружбу от friendId, указываем статус CONFIRMED по дружбе у обоих
        } else {
            if (status.get(0) == 1) {
                sqlQuery = "MERGE INTO FRIENDS(USER_ID, FRIEND_ID, FRIEND_STATUS_ID) values (?, ?, ?)";
                jdbcTemplate.update(sqlQuery, userId, friendId, 2);
                jdbcTemplate.update(sqlQuery, friendId, userId, 2);
                log.info("Пользователь ID {} подтвердил дружбу с ID {} (CONFIRMED)", userId, friendId);
            }
        }
    }

    @Override
    public List<User> getFriends(int userId) {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        final List<User> friends = jdbcTemplate.query(sqlQuery, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                int friendId = rs.getInt("friend_id");
                return getById(friendId);
            }
        }, userId);
        return friends;
    }

    @Override
    public List<User> getCommonFriends(int userId, int otherId) {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ? " +
                                "INTERSECT " +
                                "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        final List<User> commonFriends = jdbcTemplate.query(sqlQuery, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                int id = rs.getInt("friend_id");
                return getById(id);
            }
        }, userId, otherId);
        return commonFriends;
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sqlQuery = "DELETE FROM FRIENDS WHERE (USER_ID, FRIEND_ID) = (?,?)";
        jdbcTemplate.update(sqlQuery, userId, friendId);
        jdbcTemplate.update(sqlQuery, userId, friendId);
    }

    private User createUser(ResultSet rs) throws SQLException {
        return new User(rs.getInt("user_id"),
                rs.getString("email"),
                rs.getString("login"),
                rs.getString("name"),
                rs.getDate("birthday").toLocalDate(),
                applyFriends(rs.getInt("user_id")));
    }

    private List<Integer> applyFriends (int userId) {
        final String sqlQuery = "SELECT FRIEND_ID FROM FRIENDS WHERE USER_ID = ?";
        final List<Integer> friends = jdbcTemplate.query(sqlQuery, new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                return rs.getInt("friend_id");
            }
        }, userId);
        return friends;
    }
}
