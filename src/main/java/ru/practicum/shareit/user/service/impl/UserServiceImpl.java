package ru.practicum.shareit.user.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.excemples.NotExistUserException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserStorage userStorage;

    @Override
    public UserDto crateUser(UserDto userDto) {
        User user = userStorage.save(UserMapper.convertToUser(userDto));
        return UserMapper.convertUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.findAll().stream()
                .map(UserMapper::convertUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto getUserById(long id) {
        return UserMapper.convertUserDto(userStorage.findById(id).orElseThrow(
                () -> new NotExistUserException("Not found user")
        ));
    }

    @Override
    public void deleteUserById(long id) {
        userStorage.deleteById(id);
    }

    @Override
    public UserDto updateUser(long id, UserDto userDto) {
        User updateUser = UserMapper.convertToUser(userDto);
        updateUser.setId(id);
        updateFieldUser(updateUser, id);
        userStorage.save(updateUser);
        return UserMapper.convertUserDto(updateUser);
    }

    private void updateFieldUser(User user, long id) {
        User oldUser = userStorage.findById(id).orElseThrow(
                () -> new NotExistUserException("Not found user"));
        if (user.getEmail() == null) {
            user.setEmail(oldUser.getEmail());
        }
        if (user.getName() == null) {
            user.setName(oldUser.getName());
        }
    }
}
