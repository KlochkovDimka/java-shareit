package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * TODO Sprint add-item-requests.
 */
@Data
@AllArgsConstructor
public class ItemRequest {
    private long id;
    @NotBlank
    private String textRequest;
    private User requestorUser;
    @FutureOrPresent
    private LocalDate startRequest;
}
