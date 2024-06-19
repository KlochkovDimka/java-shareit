package ru.practicum.shareit.booking.storage;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;


import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Slf4j
@Transactional
@Sql(scripts = "/truncate.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class BookingStorageTest {
    @Autowired
    BookingStorage bookingStorage;
    @Autowired
    UserStorage userStorage;
    @Autowired
    ItemStorage itemStorage;
    @Autowired
    ItemRequestStorage itemRequestStorage;

    @BeforeEach
    public void addItem() {
        User user = User.builder()
                .email("user@email.com")
                .name("user")
                .build();
        userStorage.save(user);

        ItemRequest itemRequest = ItemRequest.builder()
                .textRequest("itemRequest")
                .requestorUser(user)
                .startRequest(LocalDateTime.now())
                .build();
        itemRequestStorage.save(itemRequest);

        Item item = Item.builder()
                .name("Item")
                .description("ItemDescription")
                .available(true)
                .ownerId(user)
                .requestId(itemRequest)
                .build();
        itemStorage.save(item);

        Booking bookingRejected = Booking.builder()
                .start(LocalDateTime.of(2024, 2, 2, 12, 12, 12))
                .end(LocalDateTime.of(2024, 3, 2, 12, 12, 12))
                .item(item)
                .booker(user)
                .status(Status.REJECTED)
                .build();
        bookingStorage.save(bookingRejected);

        Booking bookingWaiting = Booking.builder()
                .start(LocalDateTime.of(2024, 4, 2, 12, 12, 12))
                .end(LocalDateTime.of(2024, 4, 3, 12, 12, 12))
                .item(item)
                .booker(user)
                .status(Status.WAITING)
                .build();
        bookingStorage.save(bookingWaiting);

        Booking bookingApproved = Booking.builder()
                .start(LocalDateTime.of(2024, 5, 2, 12, 12, 12))
                .end(LocalDateTime.of(2024, 5, 3, 12, 12, 12))
                .item(item)
                .booker(user)
                .status(Status.APPROVED)
                .build();
        bookingStorage.save(bookingApproved);

        Booking bookingCanceled = Booking.builder()
                .start(LocalDateTime.of(2024, 5, 4, 12, 12, 12))
                .end(LocalDateTime.of(2024, 5, 5, 12, 12, 12))
                .item(item)
                .booker(user)
                .status(Status.CANCELED)
                .build();
        bookingStorage.save(bookingCanceled);
    }

    @Test
    void findBookingByBooker_id() {
        List<Booking> bookingList = bookingStorage
                .findBookingByBooker_id(1L, PageRequest.of(0, 1)).getContent();

        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findCurrentBookingsByUser() {
        List<Booking> bookingList = bookingStorage
                .findCurrentBookingsByUser(
                        LocalDateTime.of(2024, 4, 3, 12, 12, 12),
                        1L);
        assertNotNull(bookingList);
    }

    @Test
    void findBookingsByBookerIdAndEndBeforeOrderByStartAsc() {
        List<Booking> bookingList = bookingStorage.findBookingsByBookerIdAndEndBeforeOrderByStartAsc(1L,
                LocalDateTime.of(2024, 4, 3, 12, 12, 12));

        assertEquals(bookingList.size(), 1);

    }

    @Test
    void findBookingsByBookerIdAndStartAfterOrderByStartAsc() {
        List<Booking> bookingList = bookingStorage
                .findBookingsByBookerIdAndStartAfterOrderByStartAsc(1L,
                        LocalDateTime.of(2024, 4, 3, 12, 12, 12));

        assertEquals(bookingList.size(), 2);


    }

    @Test
    void findBookingsByBookerIdAndStatusOrderByStartAsc() {
        List<Booking> bookingList = bookingStorage
                .findBookingsByBookerIdAndStatusOrderByStartAsc(1L, Status.REJECTED);

        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findBookingsByItemIdAndBookerIdAndEndBefore() {
        List<Booking> bookingList = bookingStorage
                .findBookingsByItemIdAndBookerIdAndEndBefore(1L,
                        1L,
                        LocalDateTime.of(2024, 4, 3, 12, 12, 12));
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findAllBookingsByOwnerId() {
        List<Booking> bookingList = bookingStorage
                .findAllBookingsByOwnerId(1L, PageRequest.of(0, 1)).getContent();

        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findBookersByOwnerIdFuture() {
        List<Booking> bookingList = bookingStorage
                .findBookersByOwnerIdFuture(1L,
                        LocalDateTime.of(2024, 4, 3, 12, 12, 12),
                        PageRequest.of(0, 1)).getContent();

        assertEquals(bookingList.size(), 1);

    }

    @Test
    void findBookersByOwnerIdCurrent() {
        List<Booking> bookingList = bookingStorage
                .findBookersByOwnerIdCurrent(1L,
                        LocalDateTime.of(2024, 5, 2, 14, 12, 12),
                        PageRequest.of(0, 1)).getContent();

        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findBookersByOwnerIdPast() {
        List<Booking> bookingList = bookingStorage
                .findBookersByOwnerIdPast(1L,
                        LocalDateTime.of(2024, 4, 3, 12, 12, 12),
                        PageRequest.of(0, 1)).getContent();
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findBookersByOwnerIdStatus() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Booking> bookingList = bookingStorage
                .findBookersByOwnerIdStatus(1L, Status.REJECTED, pageable).getContent();
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void findTopByItemIdAndEndDate() {
        List<Booking> bookingList = bookingStorage
                .findTopByItemIdAndEndDate(1L,
                        LocalDateTime.of(2024, 4, 2, 14, 12, 12));
        assertEquals(bookingList.size(), 2);


    }

    @Test
    void findTopByItemIdAndEndDateAfter() {
        List<Booking> bookingList = bookingStorage
                .findTopByItemIdAndEndDateAfter(1L,
                        LocalDateTime.of(2024, 5, 3, 12, 12, 12));

        assertEquals(bookingList.size(), 1);
    }
}