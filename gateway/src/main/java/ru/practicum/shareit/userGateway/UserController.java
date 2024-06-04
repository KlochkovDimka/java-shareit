package ru.practicum.shareit.userGateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.userGateway.dto.UserDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }

    @GetMapping("{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable @Positive long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping()
    public ResponseEntity<Object> postUser(@Valid @RequestBody UserDto userDto) {
        return userClient.createUser(userDto);
    }

    @DeleteMapping("{userId}")
    public ResponseEntity<Object> deleteUserById(@PathVariable @Positive long userId) {
        return userClient.deleteUserById(userId);
    }

    @PatchMapping("{userId}")
    public ResponseEntity<Object> patchUserById(@PathVariable @Positive long userId,
                                                @RequestBody UserDto userDto) {
        return userClient.updateUser(userId, userDto);
    }
}
