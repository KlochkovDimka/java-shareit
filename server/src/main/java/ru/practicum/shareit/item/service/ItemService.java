package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;

import java.util.List;

public interface ItemService {

    ItemDto createItem(ItemDto itemDto, long userId);

    ItemWithComment getItemById(long itemId, long userId);

    List<ItemWithComment> getItemsByUserId(long userId, int from, int size);

    ItemDto updateItem(ItemDto itemDto, long userId, long itemId);

    List<ItemDto> getSearchItemDto(String text, int from, int size);

    void deleteItemById(long itemId);

    CommentDto createComment(long itemId, long userId, CommentDto commentDto);
}
