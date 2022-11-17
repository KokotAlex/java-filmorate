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
        return ResponseEntity.ok(service.getAll());
    }

    @PostMapping
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Обработка запроса на добавление пользователя {}", user);
        return ResponseEntity.ok(service.add(user));
    }

    @PutMapping
    public ResponseEntity<User> Update(@Valid @RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя {}", user);
        return ResponseEntity.ok(service.update(user));
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
        return ResponseEntity.ok(service.getMutualFriends(id, otherId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        log.info("Обработка запроса получения пользователя с id {}", id);
        return ResponseEntity.ok(service.getById(id));
    }

    @GetMapping("/{id}/friends")
    public ResponseEntity<List<User>> getFriends(@PathVariable Integer id) {
        log.info("Обработка запроса получения друзей пользователя с id {}", id);
        return ResponseEntity.ok(service.getFriends(id));
    }

}
