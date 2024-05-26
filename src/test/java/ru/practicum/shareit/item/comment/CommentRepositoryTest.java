package ru.practicum.shareit.item.comment;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ItemStorage itemStorage;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private ItemRequestStorage itemRequestStorage;

    @BeforeEach
    private void createItemAndComment() {
        User user = User.builder()
                .email("user@email.com")
                .name("user")
                .build();

        ItemRequest itemRequest = ItemRequest.builder()
                .textRequest("itemRequest")
                .requestorUser(user)
                .startRequest(LocalDateTime.now())
                .build();

        Item item = Item.builder()
                .name("item")
                .description("description")
                .available(true)
                .ownerId(user)
                .requestId(itemRequest)
                .build();

        Comment comment = Comment.builder()
                .text("text")
                .item(item)
                .author(user)
                .created(LocalDateTime.now())
                .build();

        userStorage.save(user);
        itemRequestStorage.save(itemRequest);
        itemStorage.save(item);
        commentRepository.save(comment);
    }

    @Test
    void findByItem() {
        List<Comment> newComment = commentRepository.findByItem(4L);
        assertEquals(newComment.get(0).getId(), 1);
        assertEquals(newComment.get(0).getText(), "text");
    }

    @Test
    void findByItem_wrongItemId_emptyList() {
        List<Comment> newComment = commentRepository.findByItem(99L);
        assertEquals(newComment.size(), 0);
    }

}