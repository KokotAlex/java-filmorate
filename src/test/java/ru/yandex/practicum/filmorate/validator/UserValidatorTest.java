package ru.yandex.practicum.filmorate.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserValidatorTest {

   @Test
   @DisplayName("Проверка на валидность пользователя.")
   void userValidate() {
       String login = "dolore";
       String name = "Nick Name";
       String email = "mail@mail.ru";
       LocalDate birthday = LocalDate.of(1946, 8, 20);

       // Проверим корректность валидации электронной почты.
       NullPointerException nullPointerException = assertThrows(
               NullPointerException.class,
               () -> new User(null, login, name, birthday));
       assertEquals("email не должен быть null", nullPointerException.getMessage());
       ValidationException validationException = assertThrows(
               ValidationException.class,
               () -> new User("    ", login, name, birthday));
       assertEquals("email должен быть заполнен", validationException.getMessage());
       validationException = assertThrows(
               ValidationException.class,
               () -> new User("mailmail.ru", login, name, birthday));
       assertEquals("email имеет некорректный формат.", validationException.getMessage());

       // Проверим корректность валидации логина.
       nullPointerException = assertThrows(
               NullPointerException.class,
               () -> new User(email, null, name, birthday));
       assertEquals("login не должен быть null", nullPointerException.getMessage());
       validationException = assertThrows(
               ValidationException.class,
               () -> new User(email, "  ", name, birthday));
       assertEquals("login должен быть заполнен", validationException.getMessage());

       // Проверим валидацию дня рождения.
       validationException = assertThrows(
               ValidationException.class,
               () -> new User(email, login, name, LocalDate.now().plusDays(1)));
       assertEquals("Дата рождения не может быть больше текущей даты.", validationException.getMessage());

   }

}