package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDto createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @Valid @RequestBody BookingDtoController bookingDto) {

        return bookingService.saveBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto yesOrNoOfBookingRent(@NotNull @PathVariable long bookingId,
                                           @RequestParam String approved,
                                           @RequestHeader("X-Sharer-User-Id") long userId) {
        return bookingService.yesOrNoOfBookingRent(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @NotNull @PathVariable long bookingId) {
        return bookingService.getBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(defaultValue = "ALL") String state,
                                               @RequestParam(defaultValue = "0") @Positive int from,
                                               @RequestParam(defaultValue = "10") @Positive int size) {
        return bookingService.getBookingsByState(userId, state, from, size);
    }

    @GetMapping("/owner")
    public List<BookingDto> getBookingByOwnerAndState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                      @RequestParam(defaultValue = "ALL") String state,
                                                      @RequestParam(defaultValue = "0") @Positive int from,
                                                      @RequestParam(defaultValue = "10") @Positive int size) {
        return bookingService.getBookingByOwnerAndState(userId, state, from, size);
    }
}
