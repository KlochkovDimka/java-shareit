package ru.practicum.shareit.booking.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.excemples.NotExistStatusName;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@Transactional
class BookingServiceImplTest {
    @Mock
    BookingStorage bookingStorage;
    @Mock
    UserStorage userStorage;
    @Mock
    ItemStorage itemStorage;
    @InjectMocks
    BookingServiceImpl bookingService;

    LocalDateTime start = LocalDateTime.of(2024, 5, 5, 12, 12, 12);
    LocalDateTime end = LocalDateTime.of(2024, 5, 6, 12, 12, 12);
    User user = User.builder()
            .id(1L)
            .email("user@email.com")
            .name("user")
            .build();

    User owner = User.builder()
            .id(2L)
            .email("userOwner@email.com")
            .name("user")
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .textRequest("itemRequest")
            .requestorUser(user)
            .startRequest(start)
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("ItemDescription")
            .available(true)
            .ownerId(owner)
            .requestId(itemRequest)
            .build();
    Booking booking = Booking.builder()
            .id(1L)
            .start(start)
            .end(end)
            .item(item)
            .booker(user)
            .status(Status.APPROVED)
            .build();

    BookingDtoController bookingDtoController = BookingDtoController.builder()
            .itemId(1L)
            .start(start)
            .end(end)
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
    void saveBooking_NotFoundUser() {
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotExistUserException.class,
                () -> bookingService.saveBooking(1L, bookingDtoController));
    }

    @Test
    void saveBooking_NotFoundItem() {
        when(itemStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> bookingService.saveBooking(1L, bookingDtoController));
    }

    @Test
    void yesOrNoOfBookingRent() {
        booking.setStatus(Status.WAITING);
        when(bookingStorage.save(any())).thenReturn(booking);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto newBookingDto = bookingService.yesOrNoOfBookingRent(1L, "APPROVED", 2L);

        assertEquals(newBookingDto.getId(), 1);
        assertEquals(newBookingDto.getStatus(), Status.REJECTED);
    }

    @Test
    void yesOrNoOfBookingRent_NotFoundBooking() {
        booking.setStatus(Status.WAITING);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotExistItemException.class,
                () -> bookingService.yesOrNoOfBookingRent(1L, "true", 1L));
    }

    @Test
    void getBookingById() {
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        BookingDto newBookingDto = bookingService.getBookingById(1L, 1L);

        assertEquals(newBookingDto.getId(), 1);
        assertEquals(newBookingDto.getStatus(), Status.APPROVED);
    }

    @Test
    void getBookingById_NotBookingUser() {
        user.setId(2L);
        when(bookingStorage.findById(anyLong())).thenReturn(Optional.of(booking));

        assertThrows(NotExistUserException.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void getBookingsByState_Exception() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(NotExistStatusName.class,
                () -> bookingService.getBookingsByState(1L, "ALLL", 0, 1));
    }

    @Test
    void getBookingByOwnerAndState() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        assertThrows(NotExistStatusName.class,
                () -> bookingService.getBookingByOwnerAndState(1L, "ALLL", 0, 1));
    }

    @Test
    void getBookingsByStateCurrent() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findCurrentBookingsByUser(any(), anyLong())).thenReturn(List.of(booking));

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "CURRENT", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStateAll_From4AndSize1() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingByBooker_id(anyLong(), any())).thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "ALL", 4, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStateAll_From0AndSize1() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingByBooker_id(anyLong(), any())).thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "ALL", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStatePast() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingsByBookerIdAndEndBeforeOrderByStartAsc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "PAST", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStateFuture() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingsByBookerIdAndStartAfterOrderByStartAsc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "FUTURE", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStateWaiting() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingsByBookerIdAndStatusOrderByStartAsc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "WAITING", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByStateRejected() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookingsByBookerIdAndStatusOrderByStartAsc(anyLong(), any()))
                .thenReturn(List.of(booking));

        List<BookingDto> bookingDto1 = bookingService.getBookingsByState(1L, "REJECTED", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByOwnerAndStateStateAll_From0AndSize1() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findAllBookingsByOwnerId(anyLong(), any())).thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "ALL", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingByOwnerAndStateCurrent() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookersByOwnerIdCurrent(anyLong(), any(), any())).thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "CURRENT", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingsByOwnerAndStatePast() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookersByOwnerIdPast(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "PAST", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingByOwnerAndStateFuture() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookersByOwnerIdFuture(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "FUTURE", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingByOwnerAndStateWaiting() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookersByOwnerIdStatus(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "WAITING", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingByOwnerAndStateRejected() {
        Page<Booking> bookings = new PageImpl<>(List.of(booking));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingStorage.findBookersByOwnerIdStatus(anyLong(), any(), any()))
                .thenReturn(bookings);

        List<BookingDto> bookingDto1 = bookingService.getBookingByOwnerAndState(1L, "REJECTED", 0, 1);

        assertEquals(bookingDto1.size(), 1);
    }

    @Test
    void getBookingByOwnerAndStateNotUser() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotExistUserException.class,
                () -> bookingService.getBookingsByState(2L, "All", 1, 1));
    }
}