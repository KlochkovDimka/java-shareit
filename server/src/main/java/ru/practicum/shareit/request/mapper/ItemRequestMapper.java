package ru.practicum.shareit.request.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemRequestMapper {

    public static ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = new ItemRequestDto();

        itemRequestDto.setId(itemRequest.getId());
        itemRequestDto.setDescription(itemRequest.getTextRequest());
        itemRequestDto.setCreated(itemRequest.getStartRequest());
        return itemRequestDto;
    }

    public static ItemRequest convertToItemRequest(ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = new ItemRequest();

        itemRequest.setTextRequest(itemRequestDto.getDescription());
        itemRequest.setStartRequest(itemRequestDto.getCreated());

        return itemRequest;
    }

    public static List<ItemRequestDto> convertToListItemRequestDto(List<ItemRequest> itemRequestList) {
        return itemRequestList.stream()
                .map(ItemRequestMapper::convertToItemRequestDto)
                .collect(Collectors.toList());
    }
}
