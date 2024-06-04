package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto createdItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createdItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public List<ItemRequestDto> getListItemRequestByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestService.getListItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllListItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "0") @Positive int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        return itemRequestService.getAllListItemRequest(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable long requestId) {
        return itemRequestService.getItemRequestById(userId, requestId);
    }
}