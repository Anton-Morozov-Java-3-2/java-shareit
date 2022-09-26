package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return  userService.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) throws UserNotFoundException {
        return  UserMapper.toUserDto(userService.get(id));
    }

    @PostMapping
    @Validated(ValidateOnCreateUser.class)
    public UserDto create(@Valid @RequestBody UserDto userDto) throws EmailAlreadyExistsException {
        return UserMapper.toUserDto(userService.create(UserMapper.toUser(userDto)));
    }

    @PatchMapping("/{id}")
    @Validated(ValidateOnUpdateUser.class)
    public UserDto update(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) throws
            EmailAlreadyExistsException,
            UserNotFoundException {
        return UserMapper.toUserDto(userService.update(id, UserMapper.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.delete(id);
    }
}
