package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserStorage extends Storage<User> {

    @Override
    User add(User user);

    @Override
    List<User> getAll();

    @Override
    User update(User user);

    @Override
    Optional<User> getById(Integer id);

    void insertFriend(Integer userId, Integer friendId, boolean approved);

    void updateFriend(Integer userId, Integer friendId, boolean approved);

    boolean isUser1HaveFriendUser2(Integer userId1, Integer userId2);

    boolean deleteFriend(User friend1, User friend2);

    List<User> getMutualFriends(Integer friendId1, Integer friendId2);

    Map<Integer, Boolean> getFriends(User user);

    boolean isUserNotExist(Integer userId);

}
