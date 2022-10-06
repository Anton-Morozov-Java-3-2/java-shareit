package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.ItemInfoDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private ItemRequestDto itemRequestDto;


    @Test
    void testCreate() throws Exception {

        itemRequestDto = new ItemRequestDto(1L, "Хочу стремянку",
                LocalDateTime.now().format(ItemRequestMapper.format),
                List.of(new ItemInfoDto(1L, "cтермянка", "test", true, 1L)));

        when(itemRequestService.create(any(Long.class), any(ItemRequestDto.class))).thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(itemRequestDto.getId()), Long.class)))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect((jsonPath("$.created", is(itemRequestDto.getCreated()))))
                .andExpect((jsonPath("$.items[0].id",
                        is(itemRequestDto.getItems().get(0).getId()), Long.class)));
    }

    @Test
    void testGetAllByRequestorId() throws Exception {

        List<ItemRequestDto> request = List.of(
                new ItemRequestDto(1L, "Хочу стремянку",
                LocalDateTime.now().format(ItemRequestMapper.format),
                List.of(new ItemInfoDto(1L, "cтермянка", "test", true, 1L))));

        when(itemRequestService.findAllByRequestorId(
                any(Long.class))).thenReturn(request);

        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].id", is(request.get(0).getId()), Long.class)))
                .andExpect(jsonPath("$[0].description", is(request.get(0).getDescription())))
                .andExpect((jsonPath("$[0].created", is(request.get(0).getCreated()))))
                .andExpect((jsonPath("$[0].items[0].id",
                        is(request.get(0).getItems().get(0).getId()), Long.class)));
    }

    @Test
    void testGetAll() throws Exception {

        List<ItemRequestDto> request = List.of(
                new ItemRequestDto(1L, "Хочу стремянку",
                        LocalDateTime.now().format(ItemRequestMapper.format),
                        List.of(new ItemInfoDto(1L, "cтермянка", "test", true,
                                1L))));

        when(itemRequestService.findAll(
                any(Long.class),
                any(Integer.class),
                any(Integer.class))).thenReturn(request);

        mvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect((jsonPath("$[0].id", is(request.get(0).getId()), Long.class)))
                .andExpect(jsonPath("$[0].description", is(request.get(0).getDescription())))
                .andExpect((jsonPath("$[0].created", is(request.get(0).getCreated()))))
                .andExpect((jsonPath("$[0].items[0].id",
                        is(request.get(0).getItems().get(0).getId()), Long.class)));
    }

    @Test
    void testGetById() throws Exception {

        itemRequestDto = new ItemRequestDto(1L, "Хочу стремянку",
                LocalDateTime.now().format(ItemRequestMapper.format),
                List.of(new ItemInfoDto(1L, "cтермянка", "test", true, 1L)));

        when(itemRequestService.findById(any(Long.class), any(Long.class))).thenReturn(itemRequestDto);

        mvc.perform(get("/requests/{requestId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((jsonPath("$.id", is(itemRequestDto.getId()), Long.class)))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())))
                .andExpect((jsonPath("$.created", is(itemRequestDto.getCreated()))))
                .andExpect((jsonPath("$.items[0].id",
                        is(itemRequestDto.getItems().get(0).getId()), Long.class)));
    }
}
