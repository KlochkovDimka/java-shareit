package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto createdItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getListItemRequestByUserId(Long userId);

    List<ItemRequestDto> getAllListItemRequest(long userId, int from, int size);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

}
