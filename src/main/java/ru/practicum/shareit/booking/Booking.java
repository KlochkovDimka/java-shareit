package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@Data
@AllArgsConstructor
public class Booking {
    private long id;
    @FutureOrPresent
    @NotNull
    private LocalDate start;
    @FutureOrPresent
    @NotNull
    private LocalDate end;
    private Item item;
    private User booker;
    private List<String> comments;

}
