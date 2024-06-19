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

    private static final String REQUEST_HEADER_USER = "X-Sharer-User-Id";

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createdItemRequest(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                     @Valid @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestClient.createdItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getListItemRequestByUserId(@RequestHeader(REQUEST_HEADER_USER) long userId) {
        return itemRequestClient.getListItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllListItemRequest(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                        @RequestParam(defaultValue = "0") @Positive int from,
                                                        @RequestParam(defaultValue = "10") @Positive int size) {
        return itemRequestClient.getAllListItemRequest(userId, from, size);
    }

    @GetMapping("{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                     @Positive @PathVariable long requestId) {
        return itemRequestClient.getItemRequestById(userId, requestId);
    }
}
