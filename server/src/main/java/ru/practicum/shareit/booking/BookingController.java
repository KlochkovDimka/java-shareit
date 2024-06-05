package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.service.BookingService;

import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final String REQUEST_HEADER_USER = "X-Sharer-User-Id";

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                    @RequestBody BookingDtoController bookingDto) {
        log.info("POST CREATE BOOKING {}", bookingDto);
        return bookingService.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto yesOrNoOfBookingRent(@PathVariable long bookingId,
                                           @RequestParam String approved,
                                           @RequestHeader(REQUEST_HEADER_USER) long userId) {
        return bookingService.yesOrNoOfBookingRent(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                     @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByState(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                               @RequestParam String state,
                                               @RequestParam int from,
                                               @RequestParam int size) {
        return bookingService.getBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingByOwnerAndState(@RequestHeader(REQUEST_HEADER_USER) long userId,
                                                      @RequestParam String state,
                                                      @RequestParam int from,
                                                      @RequestParam int size) {
        return bookingService.getBookingByOwnerAndState(userId, state, from, size);
    }
}
