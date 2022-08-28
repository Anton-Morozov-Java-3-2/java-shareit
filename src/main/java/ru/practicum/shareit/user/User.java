package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String email;

    public User(Long id) {
        this.id = id;
    }
}