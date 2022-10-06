package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByStartDesc(Long id, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByBookerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start,
                                                                             LocalDateTime end, Pageable pageable);

    List<Booking> findByBookerIdAndStatusOrderByStartDesc(Long id, BookingStatus status, Pageable pageable);

    List<Booking> findByItemOwnerIdAndStatusOrderByStartDesc(Long id, BookingStatus status, Pageable pageable);

    List<Booking> findAllByItemOwnerIdOrderByStartDesc(Long id, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfterOrderByStartDesc(Long id, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(Long id, LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(Long id, LocalDateTime start,
                                                                                LocalDateTime end, Pageable pageable);

    List<Booking> findAllByItemIdAndStartAfterOrderByStartDesc(Long itemId, LocalDateTime start, Pageable pageable);

    List<Booking> findAllByItemIdAndStartBeforeOrderByStartAsc(Long itemId, LocalDateTime start, Pageable pageable);

    @Query("select b from Booking b where (b.booker.id = ?1 AND b.item.id = ?2 " +
            "AND b.status = ru.practicum.shareit.booking.BookingStatus.APPROVED AND b.end < ?3)" +
            "order by b.start DESC")
    List<Booking> findAllByBookerIdAndItemId(Long bookerId, Long itemId, LocalDateTime now, Pageable pageable);
}
