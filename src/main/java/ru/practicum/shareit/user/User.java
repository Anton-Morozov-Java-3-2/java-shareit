package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Override
    public String toString() {
        return String.format("User = {id: %d, name: %s, email: %s}", id, name, email);
    }

    public boolean equals(Object o) {
        if (this == o) return  true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return this.getId().equals(user.getId())
                && this.getName().equals(user.getName())
                && this.getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(id).hashCode() + Optional.ofNullable(name).hashCode() +
                Optional.ofNullable(email).hashCode() + 30;
    }
}


