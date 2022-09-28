package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Entity
@Table(name = "items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "item_name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean isAvailable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    @Transient
    private Booking nextBooking;

    @Transient
    private Booking lastBooking;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private Set<Comment> comments = new HashSet<>();

    public Item(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Item = {id: %d, name: %s, description: %s, isAvailable: %b}",
                id, name, description, isAvailable);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;

        if (this.getRequest() != null) {
            if (item.getRequest() == null) return false;
            if (!this.getRequest().equals(item.getRequest())) return false;
        }

        return this.getId().equals(item.getId())
                && this.getName().equals(item.getName())
                && this.getDescription().equals(item.getDescription())
                && this.getIsAvailable().equals(item.getIsAvailable())
                && this.getOwner().equals(item.getOwner());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(id).hashCode() + Optional.ofNullable(name).hashCode()
                + Optional.ofNullable(description).hashCode() +  Optional.ofNullable(isAvailable).hashCode()
                + Optional.ofNullable(owner).hashCode() + 50;
    }
}
