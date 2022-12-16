package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAll() {
        // Сформируем запрос выборки пользователей и их друзей.
        final String SQL_QUERY = "SELECT USERS.USER_ID,\n" +
                "       USERS.EMAIL,\n" +
                "       USERS.LOGIN,\n" +
                "       USERS.USER_NAME,\n" +
                "       USERS.BIRTHDAY,\n" +
                "       FRIENDS.FRIEND_ID,\n" +
                "       FRIENDS.APPROVED\n" +
                "FROM USERS\n" +
                "LEFT OUTER JOIN FRIENDS on USERS.USER_ID = FRIENDS.USER_ID\n" +
                "ORDER BY USER_ID";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(SQL_QUERY);
        // Сформируем пользователей по результатам запроса.
        return createUsersFromRowSet(userRows);
    }

    @Override
    public User add(User user) {
        // Добавим пользователя в базу пользователей.
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate).
                withTableName("users").
                usingGeneratedKeyColumns("user_id").
                usingColumns("email", "login", "user_name", "birthday");

        Map<String, Object> params = new HashMap<>();
        params.put("email", user.getEmail());
        params.put("login", user.getLogin());
        params.put("user_name", user.getName());
        params.put("birthday", user.getBirthday());

        Integer UserId = simpleJdbcInsert.executeAndReturnKey(params).intValue();
        user.setId(UserId);
        user.setFriends(new HashMap<>());

        return user;
    }

    @Override
    public User update(User user) {
        // Проверим наличие пользователя в базе пользователей.
        if (isUserNotExist(user.getId())) {
            throw new NotFoundException("Пользователь: " + user + " отсутствует в базе пользователей");
        }
        // Обновим пользователя.
        final String SQL_QUERY = "UPDATE USERS SET "
                + "email = ?, login = ?, user_name = ?, birthday = ?"
                + "where USER_ID = ?";
        jdbcTemplate.update(SQL_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        return user;
    }

    @Override
    public Optional<User> getById(Integer id) {
        // Сформируем запрос выборки пользователя и его друзей по id.
        final String SQL_QUERY = "SELECT USERS.USER_ID,\n" +
                "       USERS.EMAIL,\n" +
                "       USERS.LOGIN,\n" +
                "       USERS.USER_NAME,\n" +
                "       USERS.BIRTHDAY,\n" +
                "       FRIENDS.FRIEND_ID,\n" +
                "       FRIENDS.APPROVED\n" +
                "FROM USERS\n" +
                "LEFT OUTER JOIN FRIENDS on USERS.USER_ID = FRIENDS.USER_ID\n" +
                "WHERE USERS.USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(SQL_QUERY, id);
        // Получим пользователей запроса.
        List<User> users = createUsersFromRowSet(userRows);
        if (users.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(users.get(0));
    }

    @Override
    public void insertFriend(Integer userId, Integer friendId, boolean approved) {
        final String INSERT_REQUEST = "INSERT INTO friends (user_id, friend_id, approved) VALUES (?, ?, ?)";
        jdbcTemplate.update(INSERT_REQUEST, userId, friendId, approved);
    }

    @Override
    public void updateFriend(Integer userId, Integer friendId, boolean approved) {
        final String UPDATE_REQUEST = "UPDATE friends SET approved = ? WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(UPDATE_REQUEST, approved, userId, friendId);
    }

    @Override
    public boolean isUser1HaveFriendUser2(Integer userId1, Integer userId2) {
        final String SELECT_REQUEST = "SELECT * FROM friends WHERE user_id = ? AND friend_id = ?";
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(SELECT_REQUEST, userId1, userId2);
        return friendsRows.next();
    }

    @Override
    public boolean deleteFriend(User user, User friend) {
        // Сформируем запрос удаления друга из списка друзей.
        final String SQL_QUERY = "DELETE FROM FRIENDS " +
                "WHERE (USER_ID = ? AND FRIEND_ID = ?) OR (USER_ID = ? AND FRIEND_ID = ?)";
        // удалим пользователей из друзей друг у друга.
        int result = jdbcTemplate.update(SQL_QUERY, user.getId(), friend.getId(), friend.getId(), user.getId());
        return result > 0;
    }

    @Override
    public List<User> getMutualFriends(Integer friendId1, Integer friendId2) {
        // Сформируем запрос получения идентификаторов общих друзей.
        final String SQL_QUERY = "WITH ANOTHER_FRIENDS as (\n" +
                "    SELECT FRIENDS.FRIEND_ID\n" +
                "    FROM FRIENDS\n" +
                "    WHERE USER_ID = ?\n" +
                ")\n" +
                "SELECT FRIENDS.FRIEND_ID\n" +
                "FROM FRIENDS\n" +
                "JOIN ANOTHER_FRIENDS ON FRIENDS.FRIEND_ID = ANOTHER_FRIENDS.FRIEND_ID\n" +
                "WHERE USER_ID = ?";

        // Обработаем результат запроса.
        SqlRowSet mutualFriendsRows = jdbcTemplate.queryForRowSet(SQL_QUERY, friendId1, friendId2);
        List<User> friends = new ArrayList<>();
        while (mutualFriendsRows.next()) {
            Integer friendId = mutualFriendsRows.getInt("FRIEND_ID");
            Optional<User> friend = getById(friendId);
            friend.ifPresent(friends::add);
        }

        return friends;
    }

    @Override
    public Map<Integer, Boolean> getFriends(User user) {
        // Сформируем запрос выборки друзей пользователя.
        final String SQL_QUERY = "SELECT FRIEND_ID,\n" +
                "       APPROVED\n" +
                "FROM FRIENDS\n" +
                "WHERE USER_ID = ?";
        // Обработаем запрос.
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet(SQL_QUERY, user.getId());
        Map<Integer, Boolean> friends = new HashMap<>();
        while (friendRows.next()) {
            Integer friendId = friendRows.getInt("FRIEND_ID");
            Boolean approved = friendRows.getBoolean("APPROVED");
            friends.put(friendId, approved);
        }
        
        return friends;
    }

    @Override
    public boolean isUserNotExist(Integer userId) {
        // Сформируем запрос выборки пользователя по id.
        final String SQL_QUERY = "SELECT USER_ID\n" +
                "FROM USERS\n" +
                "WHERE USERS.USER_ID = ?";
        SqlRowSet userRows = jdbcTemplate.queryForRowSet(SQL_QUERY, userId);
        // обрабатываем результат выполнения запроса
        return !userRows.next();
    }

    private List<User> createUsersFromRowSet(SqlRowSet userRows) {
        // обрабатываем результат выполнения запроса
        List<User> users = new ArrayList<>();
        int userId = 0;
        while (userRows.next()) {
            int currentUserId = userRows.getInt("USER_ID");
            if (userId != currentUserId) {
                userId = currentUserId;
                // Добавим нового пользователя в список.
                users.add(new User(userRows.getInt("USER_ID"),
                        userRows.getString("EMAIL"),
                        userRows.getString("LOGIN"),
                        userRows.getString("USER_NAME"),
                        userRows.getDate("BIRTHDAY").toLocalDate(),
                        new HashMap<>()));
            }
            int friendId = userRows.getInt("FRIEND_ID");
            if (friendId != 0) {
                // Есть данные о друге. Добавим их.
                Map<Integer, Boolean> friends = users.get(users.size() - 1).getFriends();
                friends.put(friendId, userRows.getBoolean("APPROVED"));
            }
        }
        return users;
    }

}
