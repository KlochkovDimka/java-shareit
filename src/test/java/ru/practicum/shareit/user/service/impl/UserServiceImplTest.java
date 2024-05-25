package ru.practicum.shareit.user.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserStorage userStorage;

    @InjectMocks
    private UserServiceImpl userService;

    private final User user = User.builder()
            .id(1L)
            .name("user")
            .email("user@Email.com")
            .build();

    private final UserDto userDto = UserDto.builder()
            .name("user")
            .email("user@Email.com")
            .build();

    @Test
    void crateUser() {
        when(userStorage.save(any())).thenReturn(user);

        UserDto newUserDto = userService.crateUser(userDto);

        assertEquals(newUserDto.getId(), 1);
        assertEquals(newUserDto.getName(), userDto.getName());
        assertEquals(newUserDto.getEmail(), userDto.getEmail());
    }

    @Test
    void getAllUsers() {
        List<User> users = List.of(user);
        when(userStorage.findAll()).thenReturn(users);
        List<UserDto> userDtos = userService.getAllUsers();

        assertEquals(userDtos.size(), users.size());
        assertEquals(userDtos.get(0).getId(), users.get(0).getId());
        assertEquals(userDtos.get(0).getName(), users.get(0).getName());
        assertEquals(userDtos.get(0).getEmail(), users.get(0).getEmail());
    }

    @Test
    void getUserById() {
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        UserDto newUserDto = userService.getUserById(1L);

        assertEquals(newUserDto.getId(), user.getId());
        assertEquals(newUserDto.getName(), user.getName());
        assertEquals(newUserDto.getEmail(), user.getEmail());
    }
}