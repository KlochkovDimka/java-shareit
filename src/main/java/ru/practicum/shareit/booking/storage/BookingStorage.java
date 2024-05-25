package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {
    Page<Booking> findBookingByBooker_id(long userId, Pageable pageable);

    @Query("SELECT b FROM Booking b WHERE b.booker.id = ?2 AND b.start < ?1 AND b.end > ?1 ORDER BY b.start ASC")
    List<Booking> findCurrentBookingsByUser(LocalDateTime currentDate,
                                            Long userId);

    List<Booking> findBookingsByBookerIdAndEndBeforeOrderByStartAsc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBookerIdAndStartAfterOrderByStartAsc(Long userId, LocalDateTime dateTime);

    List<Booking> findBookingsByBookerIdAndStatusOrderByStartAsc(Long userId, Status status);

    List<Booking> findBookingsByItemIdAndBookerIdAndEndBefore(Long itemId, Long bookerId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 ")
    Page<Booking> findAllBookingsByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.start > ?2")
    Page<Booking> findBookersByOwnerIdFuture(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.start < ?2 " +
            "AND b.end > ?2")
    Page<Booking> findBookersByOwnerIdCurrent(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.end < ?2")
    Page<Booking> findBookersByOwnerIdPast(Long ownerId, LocalDateTime localDateTime, Pageable pageable);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.status = ?2")
    Page<Booking> findBookersByOwnerIdStatus(Long ownerId, Status status, Pageable pageable);

    @Query("SELECT b  FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.id = ?1 AND b.end < ?2 " +
            "OR i.id = ?1 AND b.start < ?2 AND b.end > ?2 " +
            "order by b.end Desc")
    List<Booking> findTopByItemIdAndEndDate(Long itemId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item i " +
            "WHERE i.id = ?1 " +
            "AND b.start > ?2 " +
            "ORDER BY b.start ASC")
    List<Booking> findTopByItemIdAndEndDateAfter(Long itemId, LocalDateTime localDateTime);
}
