package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserDto crateUser(UserDto userDto) {
        User user = userStorage.saveUser(userMapper.convertToUser(userDto));
        return userMapper.convertUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers().stream().
                map(userMapper::convertUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUserById(long id) {
        return userMapper.convertUserDto(userStorage.getUserById(id));
    }

    @Override
    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User user = userStorage.updateUser(id, userMapper.convertToUser(userDto));
        return userMapper.convertUserDto(user);
    }
}
