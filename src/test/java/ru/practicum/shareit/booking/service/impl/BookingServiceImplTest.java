package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {
    @Mock
    private BookingStorage bookingStorage;
    @Mock
    private UserStorage userStorage;
    @Mock
    private ItemStorage itemStorage;
    @InjectMocks
    private BookingServiceImpl bookingService;

    private LocalDateTime start = LocalDateTime.of(2024, 5,5,12,12,12);
    private LocalDateTime end = LocalDateTime.of(2024, 5,6,12,12,12);
    private User user = User.builder()
            .id(1L)
            .email("user@email.com")
            .name("user")
            .build();

    private User owner = User.builder()
            .id(2L)
            .email("userOwner@email.com")
            .name("user")
            .build();

    private ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .textRequest("itemRequest")
            .requestorUser(user)
            .startRequest(start)
            .build();

    private Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("ItemDescription")
            .available(true)
            .ownerId(owner)
            .requestId(itemRequest)
            .build();

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .lastBooking(null)
            .nextBooking(null)
            .requestId(1L)
            .build();

    private Booking booking = Booking.builder()
            .id(1L)
            .start(start)
            .end(end)
            .item(item)
            .booker(user)
            .status(Status.APPROVED)
            .build();

    private BookingDto bookingDto = BookingDto.builder()
            .id(1L)
            .start(start)
            .end(end)
            .item(itemDto)
            .booker(user)
            .status(Status.APPROVED)
            .build();

    private BookingDtoController bookingDtoController = BookingDtoController.builder()
            .itemId(1L)
            .start(start)
            .end(end)
            .build();

    private Comment comment = Comment.builder()
            .id(1L)
            .text("commentDescription")
            .item(item)
            .author(user)
            .created(start)
            .build();
    @Test
    void saveBooking() {
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.save(any())).thenReturn(booking);

        BookingDto newBookingDto = bookingService.saveBooking(1L, bookingDtoController);

        assertEquals(newBookingDto.getId(), 1);
        assertEquals(newBookingDto.getItem().getId(), 1);
    }

    @Test
    void yesOrNoOfBookingRent() {
        booking.setStatus(Status.WAITING);
        when(bookingStorage.save(any())).thenReturn(booking);
        when(bookingStorage.findById(anyLong())). thenReturn(Optional.of(booking));

        BookingDto newBookingDto = bookingService.yesOrNoOfBookingRent(1L, "APPROVED", 2L);

        assertEquals(newBookingDto.getId(), 1);
        assertEquals(newBookingDto.getStatus(), Status.REJECTED);
    }

    @Test
    void getBookingById() {
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto newBookingDto = bookingService.getBookingById(1L, 1L);

        assertEquals(newBookingDto.getId(),1);
        assertEquals(newBookingDto.getStatus(),Status.APPROVED);
    }

}