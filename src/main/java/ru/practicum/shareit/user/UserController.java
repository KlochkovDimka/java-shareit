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
        //log.info("Controller: Get all users");
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public UserDto getUserById(@PathVariable("userId") long id) {
        UserDto userDto = userService.getUserById(id);
        //log.info("Controller: Get users: {}", user);
        return userDto;
    }

    @PostMapping()
    public UserDto createUser(@Valid @RequestBody UserDto userDto) {
        UserDto newUser = userService.crateUser(userDto);
        //log.info("Controller: Create user: {}", newUser);
        return newUser;
    }

    @DeleteMapping("{userId}")
    public void deleteUserById(@PathVariable("userId") long id) {
        //log.info("Controller: Delete User");
        userService.deleteUserById(id);
    }

    @PatchMapping("{userId}")
    public UserDto updateUserById(@PathVariable("userId") long id,
                                  @RequestBody UserDto userDto) {
        UserDto updateUser = userService.updateUser(id, userDto);
        //log.info("Controller: Update user: {}", updateUser);
        return updateUser;
    }
}
