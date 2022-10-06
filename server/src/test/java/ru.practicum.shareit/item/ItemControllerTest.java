package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc
public class ItemControllerTest {

    @MockBean
    private ItemService itemService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private Item item;

    private ItemDto itemDto;

    private Comment comment;

    private CommentDto commentDto;

    @Test
    void testPostComment() throws Exception {

        LocalDateTime time = LocalDateTime.now();

        comment = new Comment(1L, "Дрель", new Item("Дрель"),
                new User(1L, "user", "user@mail.ru"), time);

        commentDto =  CommentMapper.toCommentDto(comment);

        when(itemService.postComment(any(Long.class), any(Long.class), any(Comment.class))).thenReturn(comment);

        mvc.perform(post("/items/{itemId}/comment",1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto.getCreated())));
    }

    @Test
    void testCreate() throws Exception {

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                new User(1L, "user", "user@mail.ru"),
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());
        itemDto =  new ItemDto(1L, "Дрель", "Test", Boolean.TRUE,
                1L,
                null,
               null,
                new ArrayList<>());

        when(itemService.create(any(Long.class), any(Item.class))).thenReturn(item);

        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void testUpdate() throws Exception {

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                new User(1L, "user", "user@mail.ru"),
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());
        itemDto =  new ItemDto(1L, "Дрель", "Test", Boolean.TRUE,
                1L,
                null,
                null,
                new ArrayList<>());

        when(itemService.update(any(Long.class), any(Long.class), any(Item.class))).thenReturn(item);

        mvc.perform(patch("/items/{itemId}",1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void testGet() throws Exception {

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                new User(1L, "user", "user@mail.ru"),
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());
        itemDto =  new ItemDto(1L, "Дрель", "Test", Boolean.TRUE,
                1L,
                null,
                null,
                new ArrayList<>());

        when(itemService.findItemById(any(Long.class), any(Long.class))).thenReturn(item);

        mvc.perform(get("/items/{itemId}",1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$.comments", hasSize(0)));
    }

    @Test
    void testGetItemsOwner() throws Exception {

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                new User(1L, "user", "user@mail.ru"),
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());
        itemDto =  new ItemDto(1L, "Дрель", "Test", Boolean.TRUE,
                1L,
                null,
                null,
                new ArrayList<>());

        when(itemService.readAllItemsOwner(any(Long.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(item));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }

    @Test
    void testSearchItems() throws Exception {

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                new User(1L, "user", "user@mail.ru"),
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());
        itemDto =  new ItemDto(1L, "Дрель", "Test", Boolean.TRUE,
                1L,
                null,
                null,
                new ArrayList<>());

        when(itemService.searchItem(any(Long.class), any(String.class), any(Integer.class), any(Integer.class)))
                .thenReturn(List.of(item));

        mvc.perform(get("/items/search")
                        .header("X-Sharer-User-Id", 1L)
                        .param("text", "test")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto.getAvailable())))
                .andExpect(jsonPath("$[0].comments", hasSize(0)));
    }
}
