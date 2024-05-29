package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createItemToTest() throws Exception {

        String jsonStringItemOne = "{\n" +
                "    \"name\": \"Отвертка\",\n" +
                "    \"description\": \"Аккумуляторная отвертка\",\n" +
                "    \"available\": true\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonStringItemOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("2"))
                .andExpect(jsonPath("$.name").value("Отвертка"))
                .andExpect(jsonPath("$.description").value("Аккумуляторная отвертка"))
                .andExpect(jsonPath("$.available").value("true"));
    }

    @Test
    void createItemFiledDescriptionTest() throws Exception {
        String jsonString = "{\n" +
                "    \"name\": \"Дрель\",\n" +
                "    \"available\": true\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonString))
                .andExpect(status().is(400));
    }

    @Test
    void createItemFiledNameTest() throws Exception {
        String jsonString = "{\n" +
                "    \"description\": \"Простая дрель\",\n" +
                "    \"available\": true\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonString))
                .andExpect(status().is(400));
    }

    @Test
    void createItemNotFoundUserIdTest() throws Exception {
        String jsonString = "{\n" +
                "    \"name\": \"Дрель\",\n" +
                "    \"description\": \"Простая дрель\",\n" +
                "    \"available\": true\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 5)
                        .content(jsonString))
                .andExpect(status().is(404));
    }

    @Test
    void createItemWithoutUserTest() throws Exception {
        String jsonString = "{\n" +
                "    \"name\": \"Дрель\",\n" +
                "    \"description\": \"Простая дрель\",\n" +
                "    \"available\": true\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonString))
                .andExpect(status().is(400));
    }

    @Test
    void getItemByIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/items/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }

    @Test
    void getItemByFiledIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/items/3"))
                .andExpect(status().is(400));
    }

    @Test
    void getItemsByUserIdTest() throws Exception {
        String jsonStringUserOne = "{\n" +
                "    \"name\": \"user\",\n" +
                "    \"email\": \"user@user.com\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUserOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.email").value("user@user.com"));

        String jsonStringItemOne = "{\n" +
                "    \"name\": \"Отвертка\",\n" +
                "    \"description\": \"Аккумуляторная отвертка\",\n" +
                "    \"available\": true\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonStringItemOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Отвертка"))
                .andExpect(jsonPath("$.description").value("Аккумуляторная отвертка"))
                .andExpect(jsonPath("$.available").value("true"));

        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().is(200));
    }

    @Test
    void updateItem() throws Exception {
        String jsonStringItemOne = "{\n" +
                "    \"name\": \"Отвертка\",\n" +
                "    \"description\": \"Аккумуляторная отвертка\",\n" +
                "    \"available\": true\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonStringItemOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("3"))
                .andExpect(jsonPath("$.name").value("Отвертка"))
                .andExpect(jsonPath("$.description").value("Аккумуляторная отвертка"))
                .andExpect(jsonPath("$.available").value("true"));

        String jsonString = "{\n" +
                "    \"name\": \"Дрель\",\n" +
                "    \"description\": \"Аккумуляторная дрель\",\n" +
                "    \"available\": true\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .content(jsonString))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Дрель"))
                .andExpect(jsonPath("$.description").value("Аккумуляторная дрель"))
                .andExpect(jsonPath("$.available").value("true"));
    }

    @Test
    void updateItemFiledUserIdTest() throws Exception {

        String jsonString = "{\n" +
                "    \"name\": \"Дрель\",\n" +
                "    \"description\": \"Аккумуляторная дрель\",\n" +
                "    \"available\": true\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/items/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 50)
                        .content(jsonString))
                .andExpect(status().is(404));
    }

    @Test
    void searchItem() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/items/search?text=Отвертка"))
                .andExpect(status().is(200));
    }

    @Test
    void searchEmptyText() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("http://localhost:8080/items/search?text="))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
