package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item saveItem(Item item, long userId);

    Item findItemById(long itemId);

    List<Item> findAllItemByUserId(long userId);

    Item updateItemById(Item item, long userId, long itemId);

    void deleteItemById(long itemId);

    List<Item> findSearchItem(String text);
}
