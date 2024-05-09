package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    public static ItemDto convertToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        return itemDto;
    }

    /*public static ItemDto convertToItemDto(Item item,
                                           BookingItemDto lastBooking,
                                           BookingItemDto nextBooking) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(itemDto.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setLastBooking(lastBooking);
        itemDto.setNextBooking(nextBooking);
        return itemDto;
    }*/

    public static Item convertToEntity(ItemDto itemDto, User userID) {
        return new Item(itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                userID, null);
    }

    public static List<ItemDto> convertToListItemDto(List<Item> items) {
        return items.stream()
                .map(ItemMapper::convertToItemDto)
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
