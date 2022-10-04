package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;


@Entity
@Table(name = "bookings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    @Override
    public String toString() {
        return String.format("Booking = {id: %d, start: %s, end: %s, status: %s}",
                id, start.format(BookingMapper.format), end.format(BookingMapper.format), status.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;

        return this.getId().equals(booking.getId())
                && this.getItem().equals(booking.getItem())
                && this.getBooker().equals(booking.getBooker())
                && this.getStart().equals(booking.getStart())
                && this.getEnd().equals(booking.getEnd())
                && this.getStatus().equals(booking.getStatus());
    }

    @Override
    public int hashCode() {
        return Optional.ofNullable(id).hashCode() + Optional.ofNullable(item).hashCode()
                + Optional.ofNullable(booker).hashCode() + Optional.ofNullable(start).hashCode()
                + Optional.ofNullable(end).hashCode() + Optional.ofNullable(status).hashCode() + 10;
    }
}
