package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {

    User add(User user);

    List<User> getAll();

    Optional<User> update(User user);

    User getById(Integer id);

    void addFriend(User friend1, User friend2);

    boolean deleteFriend(User friend1, User friend2);

    List<User> getMutualFriends(User friend1, User friend2);

    List<User> getFriends(User user);

    boolean isUserExist(Integer userId);

}
