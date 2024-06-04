package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDtoController;
import ru.practicum.shareit.booking.service.BookingService;
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
class BookingControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    @MockBean
    BookingService bookingService;
    @MockBean
    ItemService itemService;
    @MockBean
    ItemRequestService itemRequestService;

    BookingDtoController bookingDtoController = BookingDtoController.builder()
            .itemId(1L)
            .start(LocalDateTime.of(2025, 1, 1, 12, 12, 12))
            .end(LocalDateTime.of(2025, 2, 2, 12, 12, 12))
            .build();

    @Test
    @SneakyThrows
    void createBooking() {
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .content(objectMapper.writeValueAsString(bookingDtoController))
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        verify(bookingService).saveBooking(anyLong(), any());
    }

    @Test
    @SneakyThrows
    void yesOrNoOfBookingRent() {
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        verify(bookingService).yesOrNoOfBookingRent(anyLong(), any(), anyLong());
    }

    @Test
    @SneakyThrows
    void getBookingById() {
        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
        verify(bookingService).getBookingById(anyLong(), anyLong());
    }

    @Test
    @SneakyThrows
    void getBookingsByState() {
        mockMvc.perform(get("/bookings?state=ALL&from=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(bookingService).getBookingsByState(anyLong(), any(), anyInt(), anyInt());
    }

    @Test
    @SneakyThrows
    void getBookingByOwnerAndState() {
        mockMvc.perform(get("/bookings/owner?state=ALL&from=0&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());

        verify(bookingService).getBookingByOwnerAndState(anyLong(), any(), anyInt(), anyInt());
    }
}