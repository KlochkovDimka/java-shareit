package ru.practicum.shareit.item.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingStorage;
import ru.practicum.shareit.excemples.NotExistItemException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemWithComment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestStorage;
import ru.practicum.shareit.status.Status;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemServiceImplTest {

    @Mock
    ItemStorage itemStorage;
    @Mock
    UserStorage userStorage;
    @Mock
    BookingStorage bookingStorage;
    @Mock
    ItemRequestStorage itemRequestStorage;
    @Mock
    CommentRepository commentRepository;
    @InjectMocks
    ItemServiceImpl itemService;
    User user = User.builder()
            .id(1L)
            .email("user@email.com")
            .name("user")
            .build();

    ItemRequest itemRequest = ItemRequest.builder()
            .id(1L)
            .textRequest("itemRequest")
            .requestorUser(user)
            .startRequest(LocalDateTime.now())
            .build();

    Item item = Item.builder()
            .id(1L)
            .name("item")
            .description("ItemDescription")
            .available(true)
            .ownerId(user)
            .requestId(itemRequest)
            .build();

    Booking booking = Booking.builder()
            .id(1L)
            .start(LocalDateTime.now())
            .end(LocalDateTime.now())
            .item(item)
            .booker(user)
            .status(Status.APPROVED)
            .build();

    Comment comment = Comment.builder()
            .id(1L)
            .text("commentDescription")
            .item(item)
            .author(user)
            .created(LocalDateTime.now())
            .build();

    @Test
    void createItemNullItemRequest() {

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.save(any())).thenReturn(item);

        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        ItemDto newItemDto = itemService.createItem(itemDto, 1);

        assertEquals(newItemDto.getId(), 1);
        assertEquals(newItemDto.getName(), "item");
        assertEquals(newItemDto.getDescription(), "ItemDescription");
        assertEquals(newItemDto.getAvailable(), true);
    }

    @Test
    void createItemIsPresentItemRequest() {

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));

        when(itemRequestStorage.findById(anyLong()))
                .thenReturn(Optional.of(itemRequest));

        when(itemStorage.save(any())).thenReturn(item);

        ItemDto itemDto = ItemDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .requestId(1L)
                .build();

        ItemDto newItemDto = itemService.createItem(itemDto, 1);

        assertEquals(newItemDto.getId(), 1);
        assertEquals(newItemDto.getName(), "item");
        assertEquals(newItemDto.getDescription(), "ItemDescription");
        assertEquals(newItemDto.getAvailable(), true);
        assertEquals(newItemDto.getRequestId(), 1);
    }

    @Test
    void getItemByIdEqualsOwnerId() {
        when(itemStorage.findById(any())).thenReturn(Optional.of(item));
        when(bookingStorage.findTopByItemIdAndEndDate(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingStorage.findTopByItemIdAndEndDateAfter(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(commentRepository.findByItem(anyLong()))
                .thenReturn(List.of(comment));

        ItemWithComment newItemDto = itemService.getItemById(1L, 1L);

        assertEquals(newItemDto.getId(), 1);
        assertEquals(newItemDto.getName(), "item");
        assertEquals(newItemDto.getDescription(), "ItemDescription");
        assertEquals(newItemDto.getAvailable(), true);
        assertEquals(newItemDto.getLastBooking().getId(), 1);
        assertEquals(newItemDto.getNextBooking().getId(), 1);
        assertEquals(newItemDto.getComments().size(), 1);
    }

    @Test
    void getItemByIdNotEqualsOwnerId() {
        when(itemStorage.findById(any())).thenReturn(Optional.of(item));
        when(commentRepository.findByItem(anyLong()))
                .thenReturn(List.of(comment));

        ItemWithComment newItemDto = itemService.getItemById(1L, 2L);

        assertEquals(newItemDto.getId(), 1);
        assertEquals(newItemDto.getName(), "item");
        assertEquals(newItemDto.getDescription(), "ItemDescription");
        assertEquals(newItemDto.getAvailable(), true);
        assertNull(newItemDto.getLastBooking());
        assertNull(newItemDto.getNextBooking());
        assertEquals(newItemDto.getComments().size(), 1);
    }

    @Test
    void getItemsByUserId() {
        Page<Item> itemPage = new PageImpl<>(List.of(item));

        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemStorage.findItemByOwnerId(any(), any())).thenReturn(itemPage);
        when(bookingStorage.findTopByItemIdAndEndDate(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(bookingStorage.findTopByItemIdAndEndDateAfter(anyLong(), any()))
                .thenReturn(List.of(booking));
        when(commentRepository.findByItem(anyLong()))
                .thenReturn(List.of(comment));

        List<ItemWithComment> itemWithComment = itemService.getItemsByUserId(1L, 0, 1);

        assertEquals(itemWithComment.get(0).getId(), 1);
        assertEquals(itemWithComment.get(0).getName(), "item");
        assertEquals(itemWithComment.get(0).getDescription(), "ItemDescription");
        assertEquals(itemWithComment.get(0).getAvailable(), true);
        assertEquals(itemWithComment.get(0).getLastBooking().getId(), 1);
        assertEquals(itemWithComment.get(0).getNextBooking().getId(), 1);
        assertEquals(itemWithComment.get(0).getComments().size(), 1);
    }

    @Test
    void getSearchItemDtoIsPresentList() {
        Page<Item> itemPage = new PageImpl<>(List.of(item));
        when(itemStorage.findItemsBySearchText(any(), any())).thenReturn(itemPage);
        List<ItemDto> itemDto = itemService.getSearchItemDto("item", 0, 1);

        assertEquals(itemDto.size(), 1);
    }

    @Test
    void getSearchItemDtoIsEmptyList() {

        List<ItemDto> itemDto = itemService.getSearchItemDto(" ", 0, 1);

        assertEquals(itemDto.size(), 0);
    }

    @Test
    void createCommentIsPresentBooking() {
        when(bookingStorage.findBookingsByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of(booking));
        when(commentRepository.save(any())).thenReturn(comment);
        when(itemStorage.findById(anyLong())).thenReturn(Optional.of(item));
        when(userStorage.findById(anyLong())).thenReturn(Optional.of(user));
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("user")
                .build();

        CommentDto newCommentDto = itemService.createComment(1, 1, commentDto);

        assertNotNull(newCommentDto);
    }

    @Test
    void createCommentNOtBooking() {
        when(bookingStorage.findBookingsByItemIdAndBookerIdAndEndBefore(anyLong(), anyLong(), any()))
                .thenReturn(List.of());
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .text("text")
                .authorName("user")
                .build();

        assertThrows(NotExistItemException.class, () -> itemService.createComment(1, 1, commentDto));
    }
}