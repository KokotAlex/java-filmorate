package ru.yandex.practicum.filmorate.manager;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserManager {

    List<User> getUser();

    User addUser(User user);

    User updateUser(User user);

}
