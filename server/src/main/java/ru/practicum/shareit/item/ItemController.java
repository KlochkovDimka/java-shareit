package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final String REQUEST_HEADER_USER = "X-Sharer-User-Id";

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader(REQUEST_HEADER_USER) long userId,
                            @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping("{itemId}")
    public ItemWithComment getItemById(@PathVariable long itemId,
                                       @RequestHeader(REQUEST_HEADER_USER) long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping()
    public List<ItemWithComment> getItemsByUserId(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                  @RequestParam(defaultValue = "0") @Positive int from,
                                                  @RequestParam(defaultValue = "10") @Positive int size) {
        return itemService.getItemsByUserId(userId, from, size);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader(REQUEST_HEADER_USER) long userId,
                              @PathVariable long itemId) {
        return itemService.updateItem(itemDto, userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItemDto(@RequestParam String text,
                                          @RequestParam(defaultValue = "0") int from,
                                          @RequestParam(defaultValue = "10") int size) {
        return itemService.getSearchItemDto(text, from, size);
    }

    @DeleteMapping("{itemId}")
    public void deleteItemById(@PathVariable long itemId) {
        itemService.deleteItemById(itemId);
    }

    @PostMapping("{itemId}/comment")
    public CommentDto createPost(@PathVariable long itemId,
                                 @RequestHeader(REQUEST_HEADER_USER) long userId,
                                 @RequestBody CommentDto commentDto) {
        return itemService.createComment(itemId, userId, commentDto);
    }
}
