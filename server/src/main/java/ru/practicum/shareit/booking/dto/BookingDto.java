package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * TODO Sprint add-bookings.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookingDto {
    private Long id;
    @FutureOrPresent
    @NotNull
    private LocalDateTime start;
    @FutureOrPresent
    @NotNull
    private LocalDateTime end;
    private ItemDto item;
    private User booker;
    private Status status;
}
