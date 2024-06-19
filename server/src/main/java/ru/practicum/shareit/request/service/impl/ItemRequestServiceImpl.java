package ru.practicum.shareit.request.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestStorage itemRequestStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    @Transactional
    public ItemRequestDto createdItemRequest(ItemRequestDto itemRequestDto, Long userId) {

        User user = isUserItemRequest(userId);
        itemRequestDto.setCreated(LocalDateTime.now());
        ItemRequest itemRequest = ItemRequestMapper.convertToItemRequest(itemRequestDto);
        itemRequest.setRequestorUser(user);

        return ItemRequestMapper.convertToItemRequestDto(itemRequestStorage.save(itemRequest));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getListItemRequestByUserId(Long userId) {
        isUserItemRequest(userId);

        List<ItemRequest> itemRequest = itemRequestStorage.findItemRequestByRequestorUserId(userId);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.convertToListItemRequestDto(itemRequest);

        itemRequestDtoList
                .forEach(itemRequestDto -> itemRequestDto
                        .setItems(findListItemDtoByRequestId(itemRequestDto)));

        return itemRequestDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemRequestDto> getAllListItemRequest(long userid, int from, int size) {
        PageRequest pageable = PageRequest.of(from, size);
        List<ItemRequest> requests =
                itemRequestStorage.findItemRequestByRequestorUserIdNotOrderByStartRequestDesc(userid, pageable);
        List<ItemRequestDto> itemRequestDtoList = ItemRequestMapper.convertToListItemRequestDto(requests);

        itemRequestDtoList
                .forEach(itemRequestDto -> itemRequestDto
                        .setItems(findListItemDtoByRequestId(itemRequestDto)));

        return itemRequestDtoList;
    }

    @Override
    @Transactional(readOnly = true)
    public ItemRequestDto getItemRequestById(Long userId, Long requestId) {
        isUserItemRequest(userId);

        ItemRequest itemRequest = itemRequestStorage.findById(requestId)
                .orElseThrow(() -> new NoSuchElementException("Запрос с id=" + requestId + ", не найден"));
        ItemRequestDto itemRequestDto = ItemRequestMapper.convertToItemRequestDto(itemRequest);
        itemRequestDto.setItems(
                ItemMapper.convertToListItemDtoWithRequestItem(itemStorage.findItemsByRequestId(itemRequest)));
        return itemRequestDto;
    }

    private User isUserItemRequest(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotExistUserException("Пользователя с id=" + userId + " не существует"));
    }

    private List<ItemDto> findListItemDtoByRequestId(ItemRequestDto requestDto) {
        ItemRequest requestId = ItemRequestMapper.convertToItemRequest(requestDto);
        requestId.setId(requestDto.getId());
        List<Item> items = itemStorage.findItemsByRequestId(requestId);
        return ItemMapper.convertToListItemDtoWithRequestItem(items);
    }
}

