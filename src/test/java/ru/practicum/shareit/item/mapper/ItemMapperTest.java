package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemMapperTest {
    final User user = User.builder()
            .id(1L)
            .email("uesr@email.com")
            .name("user")
            .build();
    final ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .requestorUser(user)
            .textRequest("ItemRequestText")
            .startRequest(LocalDateTime.now())
            .build();

    final Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .ownerId(user)
            .requestId(itemRequest)
            .build();

    final ItemDto itemDto = new ItemDto(1L,
            "item",
            "ItemDescription",
            true,
            null,
            null,
            1L);

    @Test
    void convertToItemDto() {
        ItemDto toItemDto = ItemMapper.convertToItemDto(item);
        assertEquals(toItemDto.getId(), item.getId());
        assertEquals(toItemDto.getName(), item.getName());
        assertEquals(toItemDto.getDescription(), item.getDescription());
        assertEquals(toItemDto.getAvailable(), item.getAvailable());
    }

    @Test
    void convertToItemDtoWithItemRequestItemId() {
        ItemDto toItemDto = ItemMapper.convertToItemDtoWithItemRequestItemId(item);
        assertEquals(toItemDto.getId(), item.getId());
        assertEquals(toItemDto.getName(), item.getName());
        assertEquals(toItemDto.getDescription(), item.getDescription());
        assertEquals(toItemDto.getAvailable(), item.getAvailable());
        assertEquals(toItemDto.getRequestId(), item.getRequestId().getId());
    }

    @Test
    void convertToEntity() {
        Item toItem = ItemMapper.convertToEntity(itemDto, user);
        assertEquals(toItem.getId(), itemDto.getId());
        assertEquals(toItem.getName(), itemDto.getName());
        assertEquals(toItem.getDescription(), itemDto.getDescription());
        assertEquals(toItem.getAvailable(), itemDto.getAvailable());
        assertEquals(toItem.getOwnerId().getId(), user.getId());
    }

    @Test
    void convertToEntityWhitRequestItem() {
        Item toItem = ItemMapper.convertToEntityWhitRequestItem(itemDto, user, itemRequest);
        assertEquals(toItem.getId(), itemDto.getId());
        assertEquals(toItem.getName(), itemDto.getName());
        assertEquals(toItem.getDescription(), itemDto.getDescription());
        assertEquals(toItem.getAvailable(), itemDto.getAvailable());
        assertEquals(toItem.getOwnerId().getId(), user.getId());
        assertEquals(toItem.getRequestId().getId(), itemRequest.getId());
    }

    @Test
    void convertToListItemDto() {
        List<Item> items = List.of(item);
        List<ItemDto> itemDtoList = ItemMapper.convertToListItemDto(items);
        assertEquals(items.size(), itemDtoList.size());
        assertEquals(items.get(0).getId(), itemDtoList.get(0).getId());
        assertEquals(items.get(0).getName(), itemDtoList.get(0).getName());
        assertEquals(items.get(0).getDescription(), itemDtoList.get(0).getDescription());
    }
}