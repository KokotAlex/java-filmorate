package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {

    Map<Integer, User> users = new HashMap<>();

    private Integer counter = 0;

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User add(User user) {
        createUser(user);
        // Добавим пользователя в базу пользователей.
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public Optional<User> update(User user) {
        // Проверим наличие пользователя в базе пользователей.
        final Integer userId = user.getId();
        if (isUserExist(userId)) {
            users.put(userId, user);
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public User getById(Integer id) {
        if (!isUserExist(id)) {
            throw new NotFoundException("User с id:" + id + " не найден.");
        }
        return users.get(id);
    }

    @Override
    public void addFriend(User friend1, User friend2) {
        // Добавим пользователей друг другу в друзья.
        friend1.getFriends().add(friend2.getId());
        friend2.getFriends().add(friend1.getId());
    }

    @Override
    public boolean deleteFriend(User friend1, User friend2) {
        Set<Integer> friends1 = friend1.getFriends();
        Set<Integer> friends2 = friend2.getFriends();
        if (!friends1.contains(friend2.getId()) || !friends2.contains(friend1.getId())) {
            return false;
        }
        return (friends1.remove(friend2.getId()) && friends2.remove(friend1.getId()));
    }

    @Override
    public List<User> getMutualFriends(User friend1, User friend2) {
        Set<Integer> friends1 = friend1.getFriends();
        Set<Integer> friends2 = friend2.getFriends();
        return friends1.stream().
                filter(friends2::contains). // Получили общие идентификаторы друзей.
                map(this::getById). // Преобразовали идентификаторы в User.
                collect(Collectors.toList()); // Преобразовали результат в список.
    }

    @Override
    public List<User> getFriends(User user) {
        return  user.getFriends(). // Получили Set идентификаторов друзей.
                stream(). // Преобразовали в stream
                map(this::getById). // Преобразовали идентификаторы в User.
                collect(Collectors.toList()); // Преобразовали результат в список.
    }

    @Override
    public boolean isUserExist(Integer userId) {
        return users.containsKey(userId);
    }

    private void createUser(User user) {
        user.setId(++counter);
    }

}