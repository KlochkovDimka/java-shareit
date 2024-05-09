package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.status.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingStorage extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByBooker_id(long userId);

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
    List<Booking> findAllBookingsByOwnerId(Long ownerId);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.start > ?2")
    List<Booking> findBookersByOwnerIdFuture(Long ownerId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.start < ?2 " +
            "AND b.end > ?2")
    List<Booking> findBookersByOwnerIdCurrent(Long ownerId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.end < ?2")
    List<Booking> findBookersByOwnerIdPast(Long ownerId, LocalDateTime localDateTime);

    @Query("SELECT b FROM Booking b " +
            "JOIN b.item s " +
            "JOIN s.ownerId u " +
            "WHERE u.id = ?1 " +
            "AND b.status = ?2")
    List<Booking> findBookersByOwnerIdStatus(Long ownerId, Status status);

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
