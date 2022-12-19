package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {

    private final UserDbStorage storage;

    User user1InMemory;
    User user2InMemory;

    @BeforeEach
    void BeforeEach() {
        user1InMemory = new User();
        user1InMemory.setEmail("qwerty@ya.ru");
        user1InMemory.setLogin("qwerty");
        user1InMemory.setName("Александр");
        user1InMemory.setBirthday(LocalDate.of(1970,1,2));

        user2InMemory = new User();
        user2InMemory.setEmail("asdf@ya.ru");
        user2InMemory.setLogin("login");
        user2InMemory.setName("Коля");
        user2InMemory.setBirthday(LocalDate.of(1985,3,4));
    }

    @Test
    void UserDbStorageTest() {
        // Этап 1. Проверим, что запись пользователя происходит без ошибок.
        User dbUser1 = assertDoesNotThrow(() -> storage.add(user1InMemory));
        assertEquals(1, storage.getAll().size());

        // Этап 2. Проверим, что созданный пользователь в БД имеет поля, аналогичные полям пользователя в памяти.
        assertEquals(user1InMemory.getEmail(), dbUser1.getEmail());
        assertEquals(user1InMemory.getLogin(), dbUser1.getLogin());
        assertEquals(user1InMemory.getName(), dbUser1.getName());
        assertEquals(user1InMemory.getBirthday(), dbUser1.getBirthday());

        // Этап 3. Сохраним в БД второго пользователя и убедимся, что запрос по id возвращает верного.
        User dbUser2 = storage.add(user2InMemory);
        assertEquals(dbUser2.getId(), (storage.getById(dbUser2.getId()).get().getId()));

        // Этап 4. Проверим, что в базе сохранено 2 пользователя.
        List<User> dbUsers = storage.getAll();
        assertEquals(2, dbUsers.size());

        // Этап 5. Проверим, что данные пользователя в базе обновляются.
        dbUser2.setName("Николай");
        User updatedDbUser2 = storage.update(dbUser2);
        assertEquals("Николай", updatedDbUser2.getName());
    }

}