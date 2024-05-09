package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemStorage extends JpaRepository<Item, Long> {

    List<Item> findItemByOwnerId(User ownerId);

    @Query("SELECT i FROM Item i " +
            "WHERE lower(i.name) LIKE %:text% OR lower(i.description) " +
            "LIKE %:text% AND i.available = true")
    public List<Item> findItemsBySearchText(@Param("text") String text);

}
