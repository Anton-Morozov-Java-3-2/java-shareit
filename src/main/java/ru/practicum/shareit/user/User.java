package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    public User() {
    }
}