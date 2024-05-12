package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;

import java.util.List;

public interface BookingService {

    BookingDto saveBooking(Long userId, BookingDtoController bookingDto);

    BookingDto yesOrNoOfBookingRent(Long bookingId, String approved, Long userId);

    BookingDto getBookingById(Long userId, Long bookingId);

    List<BookingDto> getBookingsByState(Long userId, String state);

    List<BookingDto> getBookingByOwnerAndState(Long ownerId, String state);

}
