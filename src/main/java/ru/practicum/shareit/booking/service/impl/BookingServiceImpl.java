package ru.practicum.shareit.booking.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.excemples.NotExistStatusName;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final BookingStorage bookingStorage;
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    @Transactional(rollbackOn = {NotExistItemException.class, NotExistUserException.class})
    public BookingDto saveBooking(Long userId, BookingDtoController bookingDto) {

        Item item = itemStorage.findById(bookingDto.getItemId()).get();
        User user = userStorage.findById(userId).get();

        isValidDate(bookingDto.getStart(), bookingDto.getEnd());

        if (!item.getAvailable()) {
            throw new NotExistItemException("Вещь не доступна для бронирования");
        }
        if (userId == item.getOwnerId().getId()) {
            throw new NotExistUserException("Нельзя бронировать свою вещь");
        }
        item.setAvailable(false);
        itemStorage.save(item);

        Booking booking = BookingMapper.convertToBooking(bookingDto, item, user);
        booking.setStatus(Status.WAITING);

        return BookingMapper.convertToBookingDto(bookingStorage.save(booking));
    }

    @Override
    @Transactional(rollbackOn = {NotExistItemException.class, NotExistUserException.class})
    public BookingDto yesOrNoOfBookingRent(Long bookingId, String approved, Long userId) {

        boolean isApproved = Boolean.parseBoolean(approved);

        Booking booking = bookingStorage.findById(bookingId).get();
        if (!Objects.equals(booking.getItem().getOwnerId().getId(), userId)) {
            throw new NotExistUserException("Подтвердить может только хозяин вещи");
        }
        if (booking.getItem().getAvailable() && booking.getStatus() == Status.APPROVED) {
            throw new NotExistItemException("Уже подтвержденное");
        }

        Item item = booking.getItem();
        item.setAvailable(isApproved);
        booking.setItem(item);
        if (!isApproved) {
            item.setAvailable(true);
            booking.setStatus(Status.REJECTED);
            itemStorage.save(item);
        } else {
            booking.setStatus(Status.APPROVED);
        }
        itemStorage.save(item);
        return BookingMapper.convertToBookingDto(bookingStorage.save(booking));
    }

    @Override
    public BookingDto getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingStorage.findById(bookingId).get();
        if (userId != booking.getBooker().getId() &&
                userId != booking.getItem().getOwnerId().getId()) {
            throw new NotExistUserException("Conflict user");
        }
        return BookingMapper.convertToBookingDto(booking);
    }

    @Override
    public List<BookingDto> getBookingsByState(Long userId, String state, int from, int size) {
        PageRequest pageable = PageRequest.of(from, size, Sort.by("start").descending());
        if (userStorage.findById(userId).isEmpty()) {
            throw new NotExistUserException("Not user");
        }
        switch (state) {
            case "ALL":
                Page<Booking> bookingList = bookingStorage.findBookingByBooker_id(userId, pageable);
                long lastPage = bookingList.getTotalElements() / bookingList.getSize();
                if (lastPage <= from - 1) {
                    return BookingMapper.convertToListBookingDto(
                            bookingStorage.findBookingByBooker_id(userId, PageRequest.of(
                                    (int) lastPage,
                                    size,
                                    Sort.by("start").descending())).getContent());
                }
                return BookingMapper.convertToListBookingDto(bookingList.getContent());
            case "CURRENT":
                return BookingMapper.convertToListBookingDto(bookingStorage
                        .findCurrentBookingsByUser(LocalDateTime.now(), userId));
            case "PAST":
                return BookingMapper.convertToListBookingDto(bookingStorage
                        .findBookingsByBookerIdAndEndBeforeOrderByStartAsc(userId, LocalDateTime.now()));
            case "FUTURE":
                return BookingMapper.convertToListBookingDto(bookingStorage
                        .findBookingsByBookerIdAndStartAfterOrderByStartAsc(
                                userId, LocalDateTime.now()));
            case "WAITING":
                return BookingMapper.convertToListBookingDto(bookingStorage
                        .findBookingsByBookerIdAndStatusOrderByStartAsc(userId, Status.WAITING));
            case "REJECTED":
                return BookingMapper.convertToListBookingDto(bookingStorage
                        .findBookingsByBookerIdAndStatusOrderByStartAsc(userId, Status.REJECTED));
            default:
                throw new NotExistStatusName("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<BookingDto> getBookingByOwnerAndState(Long ownerId, String state, int from, int size) {
        PageRequest pageable = PageRequest.of(from, size, Sort.by("start").descending());
        if (userStorage.findById(ownerId).isEmpty()) {
            throw new NotExistUserException("Not user");
        }
        switch (state) {
            case "ALL":
                List<Booking> bookingList = bookingStorage.findAllBookingsByOwnerId(
                                ownerId,
                                pageable)
                        .getContent();
                List<BookingDto> bookingDtoList = BookingMapper.convertToListBookingDto(bookingList);
                return bookingDtoList;
            case "CURRENT":
                return BookingMapper.convertToListBookingDto(
                        bookingStorage.findBookersByOwnerIdCurrent(
                                        ownerId,
                                        LocalDateTime.now(),
                                        pageable)
                                .getContent());
            case "PAST":
                return BookingMapper.convertToListBookingDto(
                        bookingStorage.findBookersByOwnerIdPast(
                                        ownerId,
                                        LocalDateTime.now(),
                                        pageable)
                                .getContent());
            case "FUTURE":
                return BookingMapper.convertToListBookingDto(
                        bookingStorage.findBookersByOwnerIdFuture(
                                        ownerId,
                                        LocalDateTime.now(),
                                        pageable)
                                .getContent());
            case "WAITING":
                return BookingMapper.convertToListBookingDto(
                        bookingStorage.findBookersByOwnerIdStatus(
                                        ownerId,
                                        Status.WAITING,
                                        pageable)
                                .getContent());
            case "REJECTED":
                return BookingMapper.convertToListBookingDto(
                        bookingStorage.findBookersByOwnerIdStatus(
                                        ownerId,
                                        Status.REJECTED,
                                        pageable)
                                .getContent());
            default:
                throw new NotExistStatusName("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void isValidDate(LocalDateTime start, LocalDateTime end) {
        if (end.isBefore(start) || end.equals(start)) {
            throw new NotExistItemException("Error Date");
        }
    }
}
