package ru.practicum.shareit.user.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excemples.NotExistUserEmailException;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.*;

@Repository
@Slf4j
public class UserStorageDao implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();
    private long generatedId = 1;

    @Override
    public User saveUser(User user) {
        isEmailUser(user);
        user.setId(assignId());
        users.put(user.getId(), user);
        log.info("Добавлен пользователь с id = {}", user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long id) {
        isUser(id);
        User user = users.get(id);
        return user;
    }

    @Override
    public User updateUser(long id, User user) {
        user.setId(id);
        updateFieldUser(user, id);
        users.put(id, user);
        log.info("Storage: updateUser: {}", users.get(id));
        return users.get(id);
    }

    @Override
    public void deleteUserById(Long id) {
        isUser(id);
        users.remove(id);
        log.info("Storage: delete user");
    }

    private long assignId() {
        return generatedId++;
    }

    public void isUser(long userId) {
        if (!users.containsKey(userId)) {
            throw new NotExistUserException(String.format("Пользователь с id = %s не найден", userId));
        }
    }

    private void isEmailUser(User user) {
        if (!users.isEmpty()) {
            Optional<User> isUser = users.values().stream()
                    .filter(user1 -> user1.getEmail().equals(user.getEmail()))
                    .findFirst();
            if (isUser.isPresent()) {
                throw new NotExistUserEmailException(String.format("Email = %s уже существует", user.getEmail()));
            }
        }
    }

    private void updateFieldUser(User user, long id) {
        User oldUser = users.get(id);
        if (user.getEmail() == null
                || !user.getEmail().equals(oldUser.getEmail())) {
            isEmailUser(user);
        }
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
    }

}
