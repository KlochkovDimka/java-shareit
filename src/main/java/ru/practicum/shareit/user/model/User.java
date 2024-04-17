package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */

@Data
@AllArgsConstructor
public class User {

    private long id;
    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String name;
}
