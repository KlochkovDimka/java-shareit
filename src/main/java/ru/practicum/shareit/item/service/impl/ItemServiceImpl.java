package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final ItemMapper itemMapper;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        Item item = itemStorage.saveItem(itemMapper.convertToEntity(itemDto, userId), userId);
        return itemMapper.convertToItemDto(item);
    }

    @Override
    public ItemDto getItemById(long itemId) {
        Item item = itemStorage.findItemById(itemId);
        return itemMapper.convertToItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByUserId(long userId) {
        return itemStorage.findAllItemByUserId(userId).stream()
                .map(itemMapper::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        Item item = itemStorage.updateItemById(itemMapper.convertToEntity(itemDto, userId), userId, itemId);
        return itemMapper.convertToItemDto(item);
    }

    @Override
    public List<ItemDto> getSearchItemDto(String text) {
        return itemStorage.findSearchItem(text).stream()
                .map(itemMapper::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long itemId) {
        itemStorage.deleteItemById(itemId);
    }
}
