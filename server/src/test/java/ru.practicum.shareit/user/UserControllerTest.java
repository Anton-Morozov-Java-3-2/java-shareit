package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @MockBean
    private UserServiceImpl userService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private User user;
    private UserDto userDto;


    @Test
    void testCreate() throws Exception {

        user = new User(1L, "name", "test@email.ru");
        userDto = new UserDto(null, "name", "test@email.ru");

        when(userService.create(any(User.class))).thenReturn(user);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(user.getId()), Long.class)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect((jsonPath("$.email", is(user.getEmail()))));
    }

    @Test
    void testGetAllUsers() throws Exception {

        user = new User(1L, "name", "test@email.ru");

        when(userService.getAllUsers()).thenReturn(List.of(user));

        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].id", is(user.getId()), Long.class)))
                .andExpect(jsonPath("$[0].name", is(user.getName())))
                .andExpect((jsonPath("$[0].email", is(user.getEmail()))));
    }

    @Test
    void testGet() throws Exception {

        user = new User(1L, "name", "test@email.ru");

        when(userService.get(any(Long.class))).thenReturn(user);

        mvc.perform(get("/users/{id}",1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(user.getId()), Long.class)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect((jsonPath("$.email", is(user.getEmail()))));
    }

    @Test
    void testUpdate() throws Exception {

        user = new User(1L, "name", "test@email.ru");
        userDto = new UserDto(null, "name", "test@email.ru");

        when(userService.update(any(Long.class), any(User.class))).thenReturn(user);

        mvc.perform(patch("/users/{id}",1L)
                        .content(mapper.writeValueAsString(userDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(user.getId()), Long.class)))
                .andExpect(jsonPath("$.name", is(user.getName())))
                .andExpect((jsonPath("$.email", is(user.getEmail()))));
    }

    @Test
    void testDelete() throws Exception {
        mvc.perform(delete("/users/{id}",1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));
    }
}
