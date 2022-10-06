package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.ValidateOnCreateUser;
import ru.practicum.shareit.user.dto.ValidateOnUpdateUser;

import javax.validation.Valid;

@Controller
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Slf4j
@Validated
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return  userClient.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") Long id) {
        log.info("Get user with id= {}", id);
        return  userClient.getById(id);
    }

    @PostMapping
    @Validated(ValidateOnCreateUser.class)
    public ResponseEntity<Object> create(@Valid @RequestBody UserDto userDto) {
        log.info("Create user {}", userDto);
        return userClient.create(userDto);
    }

    @PatchMapping("/{id}")
    @Validated(ValidateOnUpdateUser.class)
    public ResponseEntity<Object> update(@PathVariable("id") Long id,
                                         @Valid @RequestBody UserDto userDto) {
        log.info("Update userId={}, user {}", id, userDto);
        return userClient.update(id, userDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") Long id) {
        log.info("Delete userId= {}", id);
        return userClient.delete(id);
    }
}
