package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Slf4j
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class ItemStorageTest {

    @Autowired
    ItemStorage itemStorage;
    @Autowired
    UserStorage userStorage;
    @Autowired
    ItemRequestStorage itemRequestStorage;

    @BeforeEach
    public void addItem() {
        User user = User.builder()
                .email("user@email.com")
                .name("user")
                .build();
        userStorage.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .textRequest("itemRequest")
                .requestorUser(user)
                .startRequest(LocalDateTime.now())
                .build();
        itemRequestStorage.save(itemRequest);

        Item item = Item.builder()
                .name("Item")
                .description("ItemDescription")
                .available(true)
                .ownerId(user)
                .requestId(itemRequest)
                .build();
        itemStorage.save(item);
    }

    @Test
    void findItemByOwnerId() {
        Pageable pageable = PageRequest.of(0, 1);
        User owner = userStorage.findById(1L).get();
        List<Item> items = itemStorage.findItemByOwnerIdOrderById(owner, pageable).getContent();
        assertEquals(items.size(), 1);
        assertEquals(items.get(0).getId(), 1);
        assertEquals(items.get(0).getName(), "Item");
        assertEquals(items.get(0).getDescription(), "ItemDescription");
        assertEquals(items.get(0).getAvailable(), true);
        assertEquals(items.get(0).getOwnerId().getId(), 1);
    }

    @Test
    void findItemsBySearchText() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Item> itemDtoList = itemStorage.findItemsBySearchText("item", pageable).getContent();
        Item item = itemDtoList.get(0);
        assertEquals(itemDtoList.size(), 1);
        assertEquals(item.getId(), 1);
    }

    @Test
    void findItemsByRequestId() {
        Optional<ItemRequest> itemRequest = itemRequestStorage.findById(1L);
        List<Item> items = itemStorage.findItemsByRequestId(itemRequest.get());
        assertEquals(1, items.size());
    }

    @AfterEach
    void delete() {
        itemStorage.deleteAll();
        userStorage.deleteAll();
        itemRequestStorage.deleteAll();
    }
}