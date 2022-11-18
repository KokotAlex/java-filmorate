package ru.yandex.practicum.filmorate.servise;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements ModelService<User> {

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
        User addUser = storage.add(user);
        log.debug("Окончание добавления пользователя {}", addUser);
        return addUser;
    }

    @Override
    public User update(User user) {
        log.debug("Начало обновления пользователя {}", user);
        User updatedUser = storage.update(user);
        log.debug("Окончание обновления пользователя {}", updatedUser);
        return updatedUser;
    }

    @Override
    public User getById(Integer id) {
        log.debug("Начало получения пользователя по id: {}", id);
        Optional<User> userOptional = storage.getById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("User с id:" + id + " не найден.");
        }
        log.debug("Окончание получения пользователя по id: {}", id);
        return userOptional.get();
    }

    public void addFriend(Integer friendId1, Integer friendId2) {
        log.debug("Начало добавления пользователей с id {} и {} друг другу в друзья", friendId1, friendId2);
        // Получим пользователей по переданным идентификаторам.
        User friend1 = getById(friendId1);
        User friend2 = getById(friendId2);
        // Добавим пользователей друг друга в друзья.
        storage.addFriend(friend1, friend2);
        log.debug("Окончание добавления пользователей с id {} и {} друг другу в друзья", friendId1, friendId2);
    }

    public void deleteFriend(Integer friendId1, Integer friendId2) {
        log.debug("Начало удаления пользователей с id {} и {} друг у друга из друзей", friendId1, friendId2);
        // Получим пользователей по переданным идентификаторам.
        User friend1 = getById(friendId1);
        User friend2 = getById(friendId2);
        // Удалим пользователей у друг друга из друзей.
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
        // Получим пользователей по переданным идентификаторам.
        User friend1 = getById(friendId1);
        User friend2 = getById(friendId2);
        // Получим общих пользователей.
        List<User> friends = storage.getMutualFriends(friend1, friend2);
        log.debug("Окончание получения общих друзей для пользователей с id {} и {}", friendId1, friendId2);
        return friends;
    }

    public List<User> getFriends(Integer userId) {
        log.debug("Начало получения друзей пользователей с id {}", userId);
        User User = getById(userId);
        List<User> friends = storage.getFriends(User);
        log.debug("Окончание получения друзей пользователей с id {}", userId);
        return friends;
    }

}
