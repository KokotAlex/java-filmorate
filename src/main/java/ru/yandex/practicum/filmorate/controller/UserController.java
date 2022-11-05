package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.UserManager;
import ru.yandex.practicum.filmorate.manager.UserManagerInMemory;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserManager manager = new UserManagerInMemory();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUser() {
        log.info("Обработка запроса на получение всех пользователей.");
        return manager.getUser();
    }

    @PostMapping
    public User postUser(@RequestBody User user) {
        log.info("Обработка запроса на добавление пользователя {}", user);
        return manager.addUser(user);
    }

    @PutMapping
    public User putUser(@RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя {}", user);
        return manager.updateUser(user);
    }

}
