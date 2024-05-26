package ru.practicum.shareit.request.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Transactional
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ItemRequestStorageTest {

    @Autowired
    private ItemRequestStorage itemRequestStorage;
    @Autowired
    private UserStorage userStorage;

    @BeforeEach
    private void addUser() {
        User user = new User();
        user.setName("user");
        user.setEmail("user@email.com");

        userStorage.save(user);

        User userTwo = new User();
        userTwo.setName("user");
        userTwo.setEmail("userTwo@email.com");

        userStorage.save(userTwo);

        ItemRequest itemRequest = ItemRequest.builder()
                .textRequest("itemReq")
                .startRequest(LocalDateTime.now())
                .requestorUser(user)
                .build();

        itemRequestStorage.save(itemRequest);

        ItemRequest itemRequestTwo = ItemRequest.builder()
                .textRequest("itemReq")
                .startRequest(LocalDateTime.now())
                .requestorUser(userTwo)
                .build();

        itemRequestStorage.save(itemRequestTwo);
    }

    @Test
    void findItemRequestByRequestorUserId() {
        List<ItemRequest> itemRequestList = itemRequestStorage.findItemRequestByRequestorUserId(1L);
        assertEquals(itemRequestList.size(), 1);

    }

    @Test
    void findItemRequestByRequestorUserIdNotOrderByStartRequestDesc() {
        PageRequest pageable = PageRequest.of(0, 10);
        List<ItemRequest> itemRequestList = itemRequestStorage
                .findItemRequestByRequestorUserIdNotOrderByStartRequestDesc(1L, pageable);
        assertEquals(itemRequestList.size(), 1);
    }
}