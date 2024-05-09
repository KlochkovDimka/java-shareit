package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentMapper;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, long userId) {
        Item item = itemStorage.save(ItemMapper.convertToEntity(itemDto, userStorage.findById(userId).get()));
        return ItemMapper.convertToItemDto(item);
    }

    @Override
    public ItemWithComment getItemById(long itemId, long userId) {

        Item item = itemStorage.findById(itemId).get();
        ItemWithComment itemDto = ItemMapper.convertToItemWhitComment(item);

        if (userId == item.getOwnerId().getId()) {
            List<Booking> bookingsLast = bookingStorage.findTopByItemIdAndEndDate(
                            item.getId(), LocalDateTime.now()).stream()
                    .filter(booking -> booking.getStatus() == Status.APPROVED
                            || booking.getStatus() == Status.WAITING)
                    .collect(Collectors.toList());

            itemDto.setLastBooking(BookingMapper.convertToBookingItemDto(bookingsLast));

            List<Booking> bookingsNext = bookingStorage.findTopByItemIdAndEndDateAfter(
                            item.getId(), LocalDateTime.now()).stream()
                    .filter(booking -> booking.getStatus() == Status.APPROVED
                            || booking.getStatus() == Status.WAITING)
                    .collect(Collectors.toList());

            itemDto.setNextBooking(BookingMapper.convertToBookingItemDto(bookingsNext));
        }
        List<Comment> comments = commentRepository.findByItem(item.getId());
        itemDto.setComments(CommentMapper.convertListToCommentDto(comments));
        return itemDto;
    }

    @Override
    @Transactional
    public List<ItemWithComment> getItemsByUserId(long userId) {
        List<ItemWithComment> items = ItemMapper.convertListToItemWithComments(
                itemStorage.findItemByOwnerId(userStorage.findById(userId).get()));

        for (ItemWithComment item : items) {
            item.setLastBooking(BookingMapper.convertToBookingItemDto(
                    bookingStorage.findTopByItemIdAndEndDate(item.getId(), LocalDateTime.now())));

            item.setNextBooking(BookingMapper.convertToBookingItemDto(
                    bookingStorage.findTopByItemIdAndEndDateAfter(item.getId(), LocalDateTime.now())));

            List<Comment> comments = commentRepository.findByItem(item.getId());
            item.setComments(CommentMapper.convertListToCommentDto(comments));
        }
        return items;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        User user = userStorage.findById(userId).get();
        itemDto.setId(itemId);
        Item item = ItemMapper.convertToEntity(itemDto, user);
        item = updateFieldItem(item);
        return ItemMapper.convertToItemDto(itemStorage.save(item));
    }

    @Override
    public List<ItemDto> getSearchItemDto(String text) {
        if (text.isBlank()) {
            return List.of();
        }
        return itemStorage.findItemsBySearchText(text.toLowerCase()).stream()
                .map(ItemMapper::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long itemId) {
        itemStorage.deleteById(itemId);
    }

    @Override
    public CommentDto createComment(long itemId, long userId, CommentDto commentDto) {
        List<Booking> bookings = bookingStorage
                .findBookingsByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new NotExistItemException("Нельзя оставлять отзыв, если не бронировали");
        }

        Comment comment = CommentMapper.convertToComment(commentDto);
        comment.setItem(itemStorage.findById(itemId).get());
        comment.setAuthor(userStorage.findById(userId).get());
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.convertToCommentDto(
                commentRepository.save(comment));
    }

    private Item updateFieldItem(Item item) {
        Item oldItem = itemStorage.findById(item.getId()).get();
        if (item.getName() != null) {
            oldItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            oldItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            oldItem.setAvailable(item.getAvailable());
        }
        return oldItem;
    }
}
