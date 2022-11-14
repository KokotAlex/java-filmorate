package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

public class UserManagerInMemory extends ModelManagerAbs<User> {

    private final Map<Integer, User> users = new HashMap<>();

    private Integer counter = 0;

    @Override
    public List<User> get() {
        log.debug("Получение списка пользователей.");
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        log.debug("Начало добавления пользователя {}", user);
        createUser(user);
        // Добавим пользователя в базу пользователей.
        users.put(user.getId(), user);
        log.debug("Окончание добавления пользователя {}", user);
        return user;
    }

    @Override
    public User update(User user) {
        log.debug("Начало обновления пользователя {}", user);
        // Проверим наличие пользователя в базе пользователей.
        final Integer userId = user.getId();
        if (!users.containsKey(userId)) {
            throw new NotFoundException("Пользователь с id: " + userId  + " отсутствует в базе пользователей");
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
