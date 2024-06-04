package ru.practicum.shareit.requestGateway;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.requestGateway.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createdItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createdItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getListItemRequestByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemRequestClient.getListItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllListItemRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                        @RequestParam(defaultValue = "0") @Positive int from,
                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return itemRequestClient.getAllListItemRequest(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @Positive @PathVariable long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
