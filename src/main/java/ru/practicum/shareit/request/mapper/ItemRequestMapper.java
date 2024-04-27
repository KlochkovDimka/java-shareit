package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

public class ItemRequestMapper {

    public ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getTextRequest(),
                itemRequest.getRequestorUser(),
                itemRequest.getStartRequest());
    }

    public ItemRequest convertToItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(0L,
                itemRequestDto.getTextRequest(),
                itemRequestDto.getRequestorUser(),
                itemRequestDto.getStartRequest());
    }
}
