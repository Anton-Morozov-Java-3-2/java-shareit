package ru.practicum.shareit.user.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDto {
    private final Long id;
    @NotNull(groups = ValidateOnCreateUser.class)
    @NotBlank(groups = ValidateOnCreateUser.class)
    private final String name;
    @NotNull(groups = ValidateOnCreateUser.class)
    @NotBlank(groups = ValidateOnCreateUser.class)
    @Email(groups = {ValidateOnCreateUser.class, ValidateOnUpdateUser.class})
    private final String email;
}
