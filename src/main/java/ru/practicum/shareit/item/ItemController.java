package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto postItem(@RequestHeader("X-Sharer-User-Id") long userId,
                            @Valid @RequestBody ItemDto itemDto) {
        return itemService.createItem(itemDto, userId);
    }

    @GetMapping("{itemId}")
    public ItemDto getItemById(@PathVariable long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping()
    public List<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @PatchMapping("{itemId}")
    public ItemDto updateItem(@RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") long userId,
                              @PathVariable long itemId) {
        ItemDto itemDto1 = itemService.updateItem(itemDto, userId, itemId);
        log.info("ItemController: update item {}", itemDto1);
        return itemDto1;
    }

    @GetMapping("/search")
    public List<ItemDto> getSearchItemDto(@RequestParam String text) {
        return itemService.getSearchItemDto(text);
    }

    @DeleteMapping("{itemId}")
    public void deleteItemById(@PathVariable long itemId) {
        itemService.deleteItemById(itemId);
    }
}
