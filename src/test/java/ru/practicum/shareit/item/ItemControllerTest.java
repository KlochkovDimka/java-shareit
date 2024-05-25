package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;


import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class ItemControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemService itemService;
    @MockBean
    private ItemRequestService itemRequestService;

    private ItemDto itemDto = ItemDto.builder()
            .id(1L)
            .name("item")
            .description("description")
            .available(true)
            .lastBooking(null)
            .nextBooking(null)
            .requestId(1L)
            .build();

    @Test
    @SneakyThrows
    void postItem() {

        mockMvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(itemService).createItem(any(), anyLong());
    }

    @Test
    @SneakyThrows
    void getItemById() {
        mockMvc.perform(get("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(itemService).getItemById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getItemsByUserId() {
        mockMvc.perform(get("/items?from=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        verify(itemService).getItemsByUserId(anyLong(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void updateItem() {
        mockMvc.perform(patch("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(itemService).updateItem(any(), anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getSearchItemDto() {
        mockMvc.perform(get("/items/search?text=item&from=0&size=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(itemService).getSearchItemDto(any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void deleteItemById() {
        mockMvc.perform(delete("/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(itemService).deleteItemById(anyLong());
    }

    @Test
    @SneakyThrows
    void createPost() {
        CommentDto commentDto = CommentDto.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .authorName("user")
                .text("text")
                .build();
        mockMvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        verify(itemService).createComment(anyLong(), anyLong(), any());
    }
}