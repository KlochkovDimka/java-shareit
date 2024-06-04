package ru.practicum.shareit.item.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    Page<Item> findItemByOwnerIdOrderById(User ownerId, Pageable pageable);

    @Query("SELECT i FROM Item i " +
            "WHERE lower(i.name) LIKE %:text% OR lower(i.description) " +
            "LIKE %:text% AND i.available = true")
    Page<Item> findItemsBySearchText(@Param("text") String text, Pageable pageable);


    List<Item> findItemsByRequestId(ItemRequest requestId);

}
