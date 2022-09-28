package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor;

    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "request_id")
    private Set<Item> items = new HashSet<>();

    public ItemRequest(Long id) {
        this.setId(id);
    }

    @Override
    public String toString() {
        return String.format("ItemRequest = {id: %d, description: %s, created: %s}",
                id, description, created.format(ItemRequestMapper.format));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return  true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemRequest request = (ItemRequest) o;

        return  this.getId().equals(request.getId())
                && this.getDescription().equals(request.getDescription())
                && this.getRequestor().equals(request.getRequestor())
                && this.created.equals(request.getCreated());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(id).hashCode() + Optional.ofNullable(description).hashCode()
                + Optional.ofNullable(requestor).hashCode() + Optional.ofNullable(created).hashCode() + 20;
    }
}



