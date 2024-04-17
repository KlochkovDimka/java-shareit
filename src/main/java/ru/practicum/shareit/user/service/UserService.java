package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto crateUser(UserDto userDto);

    List<UserDto> getAllUsers();

    UserDto getUserById(long id);

    void deleteUserById(long id);

    UserDto updateUser(long id, UserDto userDto);
}
