package ru.yandex.practicum.filmorate.servise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements ModelService<User> {

    private static final Logger log = LoggerFactory.getLogger(FilmService.class);

    private final UserStorage storage;

    @Autowired
    public UserService(UserStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<User> getAll() {
        log.debug("Начало Получения списка пользователей.");

        List<User> users = storage.getAll();

        log.debug("Начало Получения списка пользователей.");

        return users;
    }

    @Override
    public User add(User user) {
        log.debug("Начало добавления пользователя {}", user);

        updateName(user);
        User addUser = storage.add(user);

        log.debug("Окончание добавления пользователя {}", addUser);

        return addUser;
    }

    @Override
    public User update(User user) {
        log.debug("Начало обновления пользователя {}", user);

        updateName(user);
        User updatedUser = storage.update(user);

        log.debug("Окончание обновления пользователя {}", updatedUser);

        return updatedUser;
    }

    @Override
    public User getById(Integer id) {
        log.debug("Начало получения пользователя по id: {}", id);

        User user = storage.getById(id)
                .orElseThrow(() -> new NotFoundException("User с id:" + id + " не найден."));

        log.debug("Окончание получения пользователя по id: {}", id);

        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        log.debug("Начало добавления пользователей с id {} и {} друг другу в друзья", userId, friendId);

        // Проверим существование пользователей в БД.
        if (storage.isUserNotExist(userId)) {
            throw new NotFoundException("Не найден пользователь с id: " + userId);
        }
        if (storage.isUserNotExist(friendId)) {
            throw new NotFoundException("Не найден пользователь c id: " + friendId);
        }

        // Т.к. user сделал запрос дружбы с friend, то friend для user автоматически является подтвержденным другом.
        // Добавим пользователя friend в качестве подтвержденного пользователя для user
        if (storage.isUserHaveAFriend(userId, friendId)) {
            storage.updateFriend(userId, friendId, true);
        } else {
            storage.insertFriend(userId, friendId, true);
        }

        // Т.к. user сделал запрос дружбы с friend, то нужно проверить, является user другом для friend.
        // Если является (неважно в каком статусе), то ничего не делаем.
        // Иначе фиксируем запись, что user для friend на текущий момент является неподтвержденным другом.
        if (!storage.isUserHaveAFriend(friendId, userId)) {
            storage.insertFriend(friendId, userId, false);
        }

        log.debug("Окончание добавления пользователей с id {} и {} друг другу в друзья", userId, friendId);
    }

    public void deleteFriend(Integer friendId1, Integer friendId2) {
        log.debug("Начало удаления пользователей с id {} и {} друг у друга из друзей", friendId1, friendId2);

        // Получим пользователей по переданным идентификаторам.
        User friend1 = getById(friendId1);
        User friend2 = getById(friendId2);
        // Удалим пользователей друг у друга из друзей.
        boolean Success = storage.deleteFriend(friend1, friend2);
        if (! Success) {
            throw new NotFoundException("пользователи с id "
                    + friendId1
                    + " и "
                    +  friendId2
                    + "не являются друзьями друг друга");
        }

        log.debug("Окончание удаления пользователей с id {} и {} друг у друга из друзей", friendId1, friendId2);
    }

    public List<User> getMutualFriends(Integer friendId1, Integer friendId2) {
        log.debug("Начало получения общих друзей для пользователей с id {} и {}", friendId1, friendId2);

        // Проверим существование пользователей.
        if (storage.isUserNotExist(friendId1)) {
            throw new NotFoundException("User с id:" + friendId1 + " не найден.");
        }
        if (storage.isUserNotExist(friendId2)) {
            throw new NotFoundException("User с id:" + friendId2 + " не найден.");
        }

        // Получим общих пользователей.
        List<User> friends = storage.getMutualFriends(friendId1, friendId2);

        log.debug("Окончание получения общих друзей для пользователей с id {} и {}", friendId1, friendId2);

        return friends;
    }

    public List<User> getFriends(Integer userId) {
        log.debug("Начало получения друзей пользователей с id {}", userId);

        User user = getById(userId);
        List<User> friends = storage.getFriends(user). // Получим друзей для user
                entrySet(). // сформируем из них итерируемую коллекцию.
                stream(). // сформируем stream
                filter(Map.Entry::getValue). // Оставим лишь подтвержденных друзей.
                map(entry -> storage.getById(entry.getKey())). // Преобразуем в поток элементов типа User.
                filter(Optional::isPresent). // Оставим только реально существующих пользователей.
                map(Optional::get). // Извлечем пользователей из Optional
                collect(Collectors.toList()); // Сформируем из оставшихся пользователей List

        log.debug("Окончание получения друзей пользователей с id {}", userId);

        return friends;
    }

    private void updateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
