package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.dto.BookingItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookingMapper {

    public static BookingDto convertToBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                ItemMapper.convertToItemDto(booking.getItem()),
                booking.getBooker(),
                booking.getStatus());
    }

    public static Booking convertToBooking(BookingDtoController bookingDto,
                                           Item item,
                                           User booker) {
        Booking booking = new Booking();

        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(booker);

        return booking;
    }

    public static List<BookingDto> convertToListBookingDto(List<Booking> bookings) {
        return bookings.stream()
                .sorted(Comparator.comparing(Booking::getStart).reversed())
                .map(BookingMapper::convertToBookingDto)
                .collect(Collectors.toList());
    }

    public static BookingItemDto convertToBookingItemDto(List<Booking> bookings) {
        if (bookings.isEmpty()) {
            return null;
        }
        Booking booking = bookings.stream()
                .findFirst()
                .get();
        BookingItemDto bookingItemDto = new BookingItemDto();
        bookingItemDto.setId(booking.getId());
        bookingItemDto.setBookerId(booking.getBooker().getId());
        return bookingItemDto;
    }
}
