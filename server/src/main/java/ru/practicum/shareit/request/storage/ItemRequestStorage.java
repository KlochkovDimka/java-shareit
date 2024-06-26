package ru.practicum.shareit.request.storage;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestStorage extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findItemRequestByRequestorUserId(Long user);


    List<ItemRequest> findItemRequestByRequestorUserIdNotOrderByStartRequestDesc(Long user, PageRequest pageable);

}
