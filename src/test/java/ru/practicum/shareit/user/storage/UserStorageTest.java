package ru.practicum.shareit.user.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserStorageTest {

    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    private void createUser() {
        User user = User.builder()
                .email("user@email.com")
                .name("user")
                .build();
        userStorage.save(user);
    }

    @Test
    void findUsersByEmail() {
        Optional<User> user = userStorage.findUsersByEmail("user@email.com");
        assertTrue(user.isPresent());
        assertEquals(user.get().getId(), 2);
        assertEquals(user.get().getName(), "user");
        assertEquals(user.get().getEmail(), "user@email.com");
    }

    @Test
    void findUsersByFailEmail() {
        Optional<User> user = userStorage.findUsersByEmail("userUser@email.com");
        assertTrue(user.isEmpty());
    }
}