package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    private long generatedId = 1L;
    private final UserStorage userStorage;

    @Override
    public Item saveItem(Item item, long userId) {
        userStorage.isUser(userId);
        item.setId(assignId());
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item findItemById(long itemId) {
        isItem(itemId);
        return items.get(itemId);
    }

    public List<Item> findAllItemByUserId(long userId) {
        return items.values().stream()
                .filter(item -> item.getUserId() == userId)
                .collect(Collectors.toList());
    }

    @Override
    public Item updateItemById(Item item, long userId, long itemId) {
        isItem(itemId);
        isConflictUserId(userId, itemId);

        item.setId(itemId);
        item.setUserId(userId);

        Item updateItem = updateFieldItem(item);
        items.put(itemId, updateItem);
        return updateItem;
    }

    @Override
    public void deleteItemById(long itemId) {
        isItem(itemId);
        items.remove(itemId);
    }

    @Override
    public List<Item> findSearchItem(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return items.values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                .filter(Item::getAvailable)
                .collect(Collectors.toList());
    }

    private Long assignId() {
        return generatedId++;
    }

    private void isItem(long itemId) {
        if (!items.containsKey(itemId)) {
            throw new NotExistItemException(String.format("Вещь в id = {} не найдена", itemId));
        }
    }

    private void isConflictUserId(long userId, long itemId) {
        if (items.get(itemId).getUserId() != userId) {
            throw new NotExistUserException(String
                    .format("Вещь с id = {}, не принадлежит пользователю с id = {}", itemId, userId));
        }
    }

    private Item updateFieldItem(Item item) {
        Item oldItem = items.get(item.getId());
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return oldItem;
    }
}
