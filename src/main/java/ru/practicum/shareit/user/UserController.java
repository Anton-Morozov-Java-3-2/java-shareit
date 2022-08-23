package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.EmailAlreadyExistsException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.mapper.DtoMapper;
import ru.practicum.shareit.user.dto.ValidateOnCreateUser;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidateOnUpdateUser;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return  userService.getAllUsers().stream().map(DtoMapper::toUserDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable("id") Long id) throws UserNotFoundException {
        return  DtoMapper.toUserDto(userService.get(id));
    }

    @PostMapping
    @Validated(ValidateOnCreateUser.class)
    public UserDto create(@Valid @RequestBody UserDto userDto) throws EmailAlreadyExistsException {
        return DtoMapper.toUserDto(userService.create(DtoMapper.toUser(userDto)));
    }

    @PatchMapping("/{id}")
    @Validated(ValidateOnUpdateUser.class)
    public UserDto update(@PathVariable("id") Long id, @Valid @RequestBody UserDto userDto) throws
            EmailAlreadyExistsException,
            UserNotFoundException {
        return DtoMapper.toUserDto(userService.update(id, DtoMapper.toUser(userDto)));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws UserNotFoundException {
        userService.delete(id);
    }
}
