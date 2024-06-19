package ru.practicum.shareit.item.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

@Data
@NoArgsConstructor
public class ItemWithComment {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingItemDto lastBooking;
    private BookingItemDto nextBooking;
    private List<CommentDto> comments;
}
