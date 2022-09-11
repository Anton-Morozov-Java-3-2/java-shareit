package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBooker_IdOrderByStartDesc(Long id);
    List<Booking> findAllByBooker_IdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);
    List<Booking> findAllByBooker_IdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);
    List<Booking> findAllByBooker_IdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);

    @Query("select b from Booking b where (b.booker.id = ?1 AND b.status = ru.practicum.shareit.booking.BookingStatus.WAITING)" +
            "order by b.start DESC")
    List<Booking> findByBookerIdAndStatusIsWaitingOrderByStartDesc(Long userId);

    @Query("select b from Booking b where (b.booker.id = ?1 AND b.status = ru.practicum.shareit.booking.BookingStatus.REJECTED)" +
            "order by b.start DESC")
    List<Booking> findByBookerIdAndStatusRejectedOrderByStartBookerDesc(Long id);

    @Query("select b from Booking b where (b.item.owner.id = ?1 AND b.status = ru.practicum.shareit.booking.BookingStatus.WAITING)" +
            "order by b.start DESC")
    List<Booking> findByOwnerIdAndStatusIsWaitingOrderByStartDesc(Long userId);

    @Query("select b from Booking b where (b.item.owner.id = ?1 AND b.status = ru.practicum.shareit.booking.BookingStatus.REJECTED)" +
            "order by b.start DESC")
    List<Booking> findByOwnerIdAndStatusRejectedOrderByStartDesc(Long id);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long id);
    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start);
    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end);
    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start, LocalDateTime end);

    List<Booking> findAllByItemIdAndStartAfterOrderByStartDesc(Long itemId, LocalDateTime start);
    List<Booking> findAllByItemIdAndStartBeforeOrderByStartAsc(Long itemId, LocalDateTime start);


    @Query("select b from Booking b where (b.booker.id = ?1 AND b.item.id = ?2 " +
            "AND b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED AND b.end < ?3)" +
            "order by b.start DESC")
    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime now);
}
