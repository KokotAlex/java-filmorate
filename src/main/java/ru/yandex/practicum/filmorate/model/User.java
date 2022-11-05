package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.practicum.filmorate.validator.UserValidator;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@ToString
public class User {
    private Integer id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private static final Logger log = LoggerFactory.getLogger(User.class);


    public User(String email, String login, String name, LocalDate birthday) {
        // Выполним проверку корректности передаваемых данных.
        this.email = email;
        this.login = login;
        if (name == null || name.isBlank()) {
            this.name = this.login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
        UserValidator.userValidate(this);
        log.info("Создан пользователь: {}", this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
