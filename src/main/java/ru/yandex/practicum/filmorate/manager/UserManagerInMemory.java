package ru.yandex.practicum.filmorate.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class UserManagerInMemory implements UserManager {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer counter = 0;
    private static final Logger log = LoggerFactory.getLogger(UserManagerInMemory.class);

    @Override
    public List<User> getUser() {
        log.debug("Получение списка пользователей.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        log.debug("Начало добавления пользователя {}", user);
        final Integer userId = user.getId();
        // Проверим наличие пользователя в базе пользователей.
        if (users.containsKey(userId)) {
            throw new ValidationException("Пользователь с id: " + userId + " уже существует");
        }
        // Если для пользователя не создан id, то создадим его.
        if (userId == null) {
            createUser(user);
        }
        // Добавим пользователя в базу пользователей.
        users.put(user.getId(), user);
        log.debug("Окончание добавления пользователя {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        log.debug("Начало обновления пользователя {}", user);
        // Проверим наличие пользователя в базе пользователей.
        final Integer userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new ValidationException("Пользователь с id: " + userId  + " отсутствует в базе пользователей");
        } else {
            users.put(userId, user);
            log.debug("Окончание обновления пользователя {}", user);
            return user;
        }
    }

    private void createUser(User user) {
        user.setId(++counter);
    }

}
