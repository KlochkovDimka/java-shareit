package ru.practicum.shareit;

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
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void createUserTest() throws Exception {
        String jsonStringUserOne = "{\n" +
                "    \"name\": \"user\",\n" +
                "    \"email\": \"user@user.com\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUserOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("user"))
                .andExpect(jsonPath("$.email").value("user@user.com"));

        String jsonStringSecondUser = "{\n" +
                "    \"name\": \"userTwo\",\n" +
                "    \"email\": \"userTwo@user.com\"\n" +
                "}";

        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringSecondUser))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.name").value("userTwo"))
                .andExpect(jsonPath("$.email").value("userTwo@user.com"));
    }

    @Test
    void updateUserEmailByIdTest() throws Exception {
        String jsonStringUpdateUserOne = "{\n" +
                "    \"email\": \"updateUserTwo@user.com\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUpdateUserOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updateUser"))
                .andExpect(jsonPath("$.email").value("updateUserTwo@user.com"));
    }

    @Test
    void updateUserNameByIdTest() throws Exception {
        String jsonStringUpdateUserOne = "{\n" +
                "    \"name\": \"updateUserTwo\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUpdateUserOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updateUserTwo"))
                .andExpect(jsonPath("$.email").value("updateUserTwo@user.com"));
    }

    @Test
    void updateUserByFiledIdTest() throws Exception {
        String jsonStringUpdateUserOne = "{\n" +
                "    \"name\": \"updateUser\",\n" +
                "    \"email\": \"updateUser@user.com\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/users/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUpdateUserOne))
                .andExpect(status().is(404));
    }

    @Test
    void updateUserByIdTest() throws Exception {
        String jsonStringUpdateUserOne = "{\n" +
                "    \"name\": \"updateUser\",\n" +
                "    \"email\": \"updateUser@user.com\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.patch("http://localhost:8080/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUpdateUserOne))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updateUser"))
                .andExpect(jsonPath("$.email").value("updateUser@user.com"));
    }

    @Test
    void getAllUSerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)));
    }


    @Test
    void getUserByFiledIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/users/5"))
                .andExpect(status().is(404));
    }

    @Test
    void getUserIdTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("updateUser"))
                .andExpect(jsonPath("$.email").value("updateUser@user.com"));
    }

    @Test
    void createUserFiledEmailTest() throws Exception {
        String jsonStringUserOne = "{\n" +
                "    \"name\": \"user\",\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUserOne))
                .andExpect(status().is(400));
    }

    @Test
    void createUserFiledNameTest() throws Exception {
        String jsonStringUserOne = "{\n" +
                "    \"email\": \"user@user.com\"\n" +
                "}";
        mockMvc.perform(MockMvcRequestBuilders.post("http://localhost:8080/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonStringUserOne))
                .andExpect(status().is(400));
    }


}
