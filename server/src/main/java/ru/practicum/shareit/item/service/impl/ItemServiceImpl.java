package ru.practicum.shareit.item.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.excemples.NotExistUserException;
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
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemStorage itemStorage;
    private final UserStorage userStorage;
    private final BookingStorage bookingStorage;
    private final ItemRequestStorage itemRequestStorage;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemDto createItem(ItemDto itemDto, long userId) {

        if (itemDto.getRequestId() == null) {
            Item item = itemStorage.save(ItemMapper.convertToEntity(itemDto,
                    findUserInStorage(userId)));
            return ItemMapper.convertToItemDto(item);
        }
        Optional<ItemRequest> itemRequest = itemRequestStorage.findById(itemDto.getRequestId());
        Item item = ItemMapper.convertToEntityWhitRequestItem(itemDto,
                findUserInStorage(userId),
                itemRequest.orElse(null));

        return ItemMapper.convertToItemDtoWithItemRequestItemId(itemStorage.save(item));
    }

    @Override
    @Transactional(readOnly = true)
    public ItemWithComment getItemById(long itemId, long userId) {

        Item item = findItemInStorage(itemId);
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
    @Transactional(readOnly = true)
    public List<ItemWithComment> getItemsByUserId(long userId, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);

        List<ItemWithComment> items = ItemMapper.convertListToItemWithComments(
                itemStorage.findItemByOwnerIdOrderById(findUserInStorage(userId), pageable).getContent());

        for (ItemWithComment item : items) {
            item.setLastBooking(BookingMapper.convertToBookingItemDto(
                    bookingStorage.findTopByItemIdAndEndDate(
                            item.getId(),
                            LocalDateTime.now())));

            item.setNextBooking(BookingMapper.convertToBookingItemDto(
                    bookingStorage.findTopByItemIdAndEndDateAfter(
                            item.getId(),
                            LocalDateTime.now())));

            List<Comment> comments = commentRepository.findByItem(item.getId());
            item.setComments(CommentMapper.convertListToCommentDto(comments));
        }
        return items;
    }

    @Override
    @Transactional()
    public ItemDto updateItem(ItemDto itemDto, long userId, long itemId) {
        User user = findUserInStorage(userId);
        itemDto.setId(itemId);
        Item item = ItemMapper.convertToEntity(itemDto, user);
        Item newItem = updateFieldItem(item);
        return ItemMapper.convertToItemDto(itemStorage.save(newItem));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemDto> getSearchItemDto(String text, int from, int size) {
        Pageable pageable = PageRequest.of(from, size);
        if (text.isBlank()) {
            return List.of();
        }
        return itemStorage.findItemsBySearchText(text.toLowerCase(), pageable).getContent().stream()
                .map(ItemMapper::convertToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteItemById(long itemId) {
        itemStorage.deleteById(itemId);
    }

    @Override
    @Transactional
    public CommentDto createComment(long itemId, long userId, CommentDto commentDto) {
        List<Booking> bookings = bookingStorage
                .findBookingsByItemIdAndBookerIdAndEndBefore(itemId, userId, LocalDateTime.now());

        if (bookings.isEmpty()) {
            throw new NotExistItemException("Нельзя оставлять отзыв, если не бронировали");
        }

        Comment comment = CommentMapper.convertToComment(commentDto);
        comment.setItem(findItemInStorage(itemId));
        comment.setAuthor(findUserInStorage(userId));
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.convertToCommentDto(
                commentRepository.save(comment));
    }

    private Item updateFieldItem(Item item) {
        Item oldItem = findItemInStorage(item.getId());
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

    private User findUserInStorage(long userId) {
        return userStorage.findById(userId).orElseThrow(
                () -> new NotExistUserException("Not found user"));
    }

    private Item findItemInStorage(long itemId) {
        return itemStorage.findById(itemId).orElseThrow(
                () -> new NoSuchElementException("Not found item"));
    }
}
