package ru.practicum.shareit.itemGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.itemGateway.comment.CommentDto;
import ru.practicum.shareit.itemGateway.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final String REQUEST_HEADER_USER = "X-Sharer-User-Id";

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> postItem(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                           @Valid @RequestBody ItemDto itemDto) {
        return itemClient.createItem(userId, itemDto);
    }

    @GetMapping("{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable @Positive long itemId,
                                              @RequestHeader(REQUEST_HEADER_USER) long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItemsByUserId(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        return itemClient.getItemsByUserId(userId, from, size);
    }

    @PatchMapping("{itemId}")
    public ResponseEntity<Object> updateItem(@RequestBody ItemDto itemDto,
                                             @RequestHeader(REQUEST_HEADER_USER) long userId,
                                             @PathVariable long itemId) {
        return itemClient.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getSearchItemDto(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                   @RequestParam String text,
                                                   @RequestParam(defaultValue = "0") @Positive int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        return itemClient.getSearchItemDto(text, userId, from, size);
    }

    @DeleteMapping("{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable long itemId) {
        return itemClient.deleteItemById(itemId);
    }

    @PostMapping("{itemId}/comment")
    public ResponseEntity<Object> createPost(@PathVariable @Positive long itemId,
                                             @RequestHeader(REQUEST_HEADER_USER) long userId,
                                             @Valid @RequestBody CommentDto commentDto) {
        return itemClient.createComment(itemId, userId, commentDto);
    }
}
