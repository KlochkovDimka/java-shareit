package ru.practicum.shareit.request.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemRequestServiceImplTest {

    @Mock
    ItemRequestStorage itemRequestStorage;
    @Mock
    UserStorage userStorage;
    @Mock
    ItemStorage itemStorage;
    @InjectMocks
    ItemRequestServiceImpl itemRequestService;

    @Test
    void createdItemRequest() {
        User user = new User(1L,
                "user@email.com",
                "user");
        ItemRequest itemRequest = new ItemRequest(1L,
                "text",
                user,
                LocalDateTime.of(2024, 5, 25, 12, 12, 12));
        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .description("text")
                .build();

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestStorage.save(any())).thenReturn(itemRequest);

        ItemRequestDto newItemRequest = itemRequestService.createdItemRequest(itemRequestDto, 1L);

        assertEquals(newItemRequest.getId(), 1);

    }

    @Test
    void getListItemRequestByUserId_whenInvoke_returnList() {
        User user = new User(1L, "user@email.com", "user");
        ItemRequest itemRequest = new ItemRequest(1L, "text", user, LocalDateTime.now());

        List<ItemRequest> itemRequestList = List.of(itemRequest);

        when(itemRequestStorage.findItemRequestByRequestorUserId(1L)).thenReturn(itemRequestList);
        when(userStorage.findById(1L)).thenReturn(Optional.of(user));

        List<ItemRequestDto> itemRequestDtos = itemRequestService.getListItemRequestByUserId(1L);

        assertEquals(itemRequestDtos.size(), itemRequestList.size());
    }

    @Test
    void getAllListItemRequest() {
        User user = new User(1L,
                "user@email.com",
                "user");
        ItemRequest itemRequest = new ItemRequest(1L,
                "text",
                user,
                LocalDateTime.of(2024, 5, 25, 12, 12, 12));
        Item item = Item.builder()
                .id(1L)
                .requestId(itemRequest)
                .ownerId(user)
                .available(true)
                .name("item")
                .build();

        List<Item> items = List.of(item);
        List<ItemRequest> itemRequestList = List.of(itemRequest);

        when(itemRequestStorage
                .findItemRequestByRequestorUserIdNotOrderByStartRequestDesc(1L, PageRequest.of(0, 1)))
                .thenReturn(itemRequestList);

        when(itemStorage.findItemsByRequestId(any())).thenReturn(items);

        List<ItemRequestDto> newItemRequestDto = itemRequestService.getAllListItemRequest(1L, 0, 1);

        assertEquals(newItemRequestDto.size(), 1);
        assertEquals(newItemRequestDto.get(0).getId(), 1);
        assertEquals(newItemRequestDto.get(0).getItems().get(0).getId(), 1);
    }

    @Test
    void getItemRequestById() {
        User user = new User(1L,
                "user@email.com",
                "user");
        ItemRequest itemRequest = new ItemRequest(1L,
                "text",
                user,
                LocalDateTime.of(2024, 5, 25, 12, 12, 12));
        Item item = Item.builder()
                .id(1L)
                .requestId(itemRequest)
                .ownerId(user)
                .available(true)
                .name("item")
                .build();

        List<Item> items = List.of(item);

        when(userStorage.findById(1L)).thenReturn(Optional.of(user));
        when(itemRequestStorage.findById(any())).thenReturn(Optional.of(itemRequest));
        when(itemStorage.findItemsByRequestId(any())).thenReturn(items);

        ItemRequestDto newItemRequestDto = itemRequestService.getItemRequestById(1L, 1L);

        assertEquals(newItemRequestDto.getId(), 1);
        assertEquals(newItemRequestDto.getDescription(), "text");
        assertEquals(newItemRequestDto.getItems().get(0).getId(), 1);
    }
}