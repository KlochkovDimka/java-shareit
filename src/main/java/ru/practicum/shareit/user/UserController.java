package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Controller: Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        UserDto userDto = userService.getUserById(userId);
        log.info("Controller: Get users: {}", userDto);
        return userDto;
    }

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        UserDto newUser = userService.crateUser(userDto);
        log.info("Controller: Create user: {}", newUser);
        return newUser;
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable long userId) {
        log.info("Controller: Delete User");
        userService.deleteUserById(userId);
    }

    @PatchMapping("{userId}")
    public UserDto updateUserById(@PathVariable long userId,
                                  @RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUser(userId, userDto);
        log.info("Controller: Update user: {}", updateUser);
        return updateUser;
    }
}
