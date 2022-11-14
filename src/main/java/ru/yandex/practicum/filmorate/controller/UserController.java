package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.manager.ModelManagerAbs;
import ru.yandex.practicum.filmorate.manager.UserManagerInMemory;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends ControllerAbs<User> {

    private final ModelManagerAbs<User> manager = new UserManagerInMemory();

    @Override
    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("Обработка запроса на получение всех пользователей.");
        return ResponseEntity.ok(manager.get());
    }

    @Override
    @PostMapping
    public ResponseEntity<User> add(@Valid @RequestBody User user) {
        log.info("Обработка запроса на добавление пользователя {}", user);
        return ResponseEntity.ok(manager.add(user));
    }

    @Override
    @PutMapping
    public ResponseEntity<User> Update(@Valid @RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя {}", user);
        return ResponseEntity.ok(manager.update(user));
    }

}
