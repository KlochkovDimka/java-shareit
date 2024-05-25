package ru.practicum.shareit.item.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemMapper {

    public static ItemDto convertToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    public static ItemDto convertToItemDtoWithItemRequestItemId(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setRequestId(item.getRequestId().getId());
        return itemDto;
    }

    public static Item convertToEntity(ItemDto itemDto,
                                       User userID) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(userID);
        return item;
    }

    public static Item convertToEntityWhitRequestItem(ItemDto itemDto,
                                                      User user,
                                                      ItemRequest itemRequest) {
        Item item = new Item();

        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwnerId(user);
        item.setRequestId(itemRequest);

        return item;
    }

    public static List<ItemDto> convertToListItemDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::convertToItemDto)
                .collect(Collectors.toList());
    }

    public static List<ItemDto> convertToListItemDtoWithRequestItem(List<Item> items) {
        return items.stream()
                .map(ItemMapper::convertToItemDtoWithItemRequestItemId)
                .collect(Collectors.toList());
    }

    public static List<ItemWithComment> convertListToItemWithComments(List<Item> items) {
        return items.stream()
                .map(ItemMapper::convertToItemWhitComment)
                .collect(Collectors.toList());
    }

    public static ItemWithComment convertToItemWhitComment(Item item) {
        ItemWithComment itemWithComment = new ItemWithComment();
        itemWithComment.setId(item.getId());
        itemWithComment.setName(item.getName());
        itemWithComment.setDescription(item.getDescription());
        itemWithComment.setAvailable(item.getAvailable());
        return itemWithComment;
    }
}
