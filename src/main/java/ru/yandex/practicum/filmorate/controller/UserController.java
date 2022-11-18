package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.servise.UserService;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("Обработка запроса на получение всех пользователей.");
        List<User> users = service.getAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Обработка запроса на добавление пользователя {}", user);
        User addedUser = service.add(user);
        return ResponseEntity.ok(addedUser);
    }

    @PutMapping
    public ResponseEntity<User> Update(@Valid @RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя {}", user);
        User updatedUser = service.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Обработка запроса на добавление в друзья пользователей с id {} и {}", id, friendId);
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info("Обработка запроса на удаление из друзей пользователей с id {} и {}", id, friendId);
        service.deleteFriend(id, friendId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Обработка запроса на получение общих друзей для пользователей с id {} и {}", id, otherId);
        List<User> friends = service.getMutualFriends(id, otherId);
        return ResponseEntity.ok(friends);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        log.info("Обработка запроса получения пользователя с id {}", id);
        User user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer id) {
        log.info("Обработка запроса получения друзей пользователя с id {}", id);
        List<User> friends = service.getFriends(id);
        return ResponseEntity.ok(friends);
    }

}
