package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    User saveUser(User user);
    List<User> getAllUsers();
    User getUserById(Long id);
    User updateUser(long id, User user);
    void deleteUserById(Long id);

    void isUser(long userId);
}
