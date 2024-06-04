package ru.practicum.shareit.bookingGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.bookingGateway.dto.BookItemRequestDto;
import ru.practicum.shareit.bookingGateway.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @RequestBody @Valid BookItemRequestDto requestDto) {

        log.info("Creating booking {}, userId={}", requestDto, userId);

        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> yesOrNoOfBookingRent(@NotNull @PathVariable long bookingId,
                                                       @NotBlank @RequestParam String approved,
                                                       @RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Patch booking {}, approved={}, userId={}", bookingId, approved, userId);
        return bookingClient.yesOrNoOfBookingRent(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                                 @NotNull @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                     @RequestParam(name = "state",
                                                             defaultValue = "ALL") String stateParam,
                                                     @PositiveOrZero @RequestParam(name = "from",
                                                             defaultValue = "0") Integer from,
                                                     @Positive @RequestParam(name = "size",
                                                             defaultValue = "10") Integer size) {

        BookingState state = BookingState.from(stateParam)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));

        log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);

        return bookingClient.getBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getBookingByOwnerAndState(@RequestHeader("X-Sharer-User-Id") long userId,
                                                            @RequestParam(defaultValue = "ALL") String state,
                                                            @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                            @RequestParam(defaultValue = "10") @Positive int size) {
        return bookingClient.getBookingByOwnerAndState(userId, state, from, size);
    }
}
