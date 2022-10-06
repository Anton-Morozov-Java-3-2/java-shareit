package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private Booking booking;

    private BookingDto bookingDto;

    private BookingInfoDto bookingInfoDto;

    private Item item;

    private User user;


    @Test
    void testCreate() throws Exception {

        user = new User(1L, "user", "user@mail.ru");

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                user,
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());

        booking = new Booking(1L, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10).withNano(0), BookingStatus.APPROVED);

        bookingDto = BookingMapper.toBookingDto(booking);

        when(bookingService.create(any(Long.class), any(Booking.class))).thenReturn(booking);

        mvc.perform(post("/bookings")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.itemId", is(bookingDto.getItemId()), Long.class))
                .andExpect(jsonPath("$.bookerId", is(bookingDto.getBookerId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingDto.getStart().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.end",
                        is(bookingDto.getEnd().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")))))
                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
    }

    @Test
    void testUpdateStatus() throws Exception {

        user = new User(1L, "user", "user@mail.ru");

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                user,
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());

        booking = new Booking(1L, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10).withNano(0), BookingStatus.APPROVED);

        bookingInfoDto = BookingMapper.toBookingInfoDto(booking);

        when(bookingService.updateStatus(any(Long.class), any(Long.class), any(Boolean.class))).thenReturn(booking);

        mvc.perform(patch("/bookings/{bookingId}", 1L)
                        .header("X-Sharer-User-Id", 1L)
                        .param("approved", "true")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.start",
                        is(bookingInfoDto.getStart())))
                .andExpect(jsonPath("$.end",
                        is(bookingInfoDto.getEnd())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus().name())))
                .andExpect(jsonPath("$.booker.id", is(bookingInfoDto.getBooker().getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDto.getItem().getId()), Long.class));
    }

    @Test
    void testGetBookingById() throws Exception {

        user = new User(1L, "user", "user@mail.ru");

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                user,
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());

        booking = new Booking(1L, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10).withNano(0), BookingStatus.APPROVED);

        bookingInfoDto = BookingMapper.toBookingInfoDto(booking);

        when(bookingService.getBookingById(any(Long.class),any(Long.class))).thenReturn(booking);

        mvc.perform(get("/bookings/{bookingId}",1L)
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$.item.id", is(bookingInfoDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingInfoDto.getStart())))
                .andExpect(jsonPath("$.end", is(bookingInfoDto.getEnd())))
                .andExpect(jsonPath("$.status", is(bookingInfoDto.getStatus().name())));
    }

    @Test
    void testGetAllBookingUser() throws Exception {

        user = new User(1L, "user", "user@mail.ru");

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                user,
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());

        booking = new Booking(1L, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10).withNano(0), BookingStatus.APPROVED);

        bookingInfoDto = BookingMapper.toBookingInfoDto(booking);

        when(bookingService.getAllBookingsUser(any(Long.class), any(String.class), any(Integer.class),
                any(Integer.class))).thenReturn(List.of(booking));

        mvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "test")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingInfoDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingInfoDto.getStart())))
                .andExpect(jsonPath("$[0].end", is(bookingInfoDto.getEnd())))
                .andExpect(jsonPath("$[0].status", is(bookingInfoDto.getStatus().name())));
    }

    @Test
    void testGetAllBookingOwner() throws Exception {

        user = new User(1L, "user", "user@mail.ru");

        item = new Item(1L, "Дрель", "Test", Boolean.TRUE,
                user,
                new ItemRequest(1L),
                null,
                null,
                new HashSet<>());

        booking = new Booking(1L, item, user, LocalDateTime.now().plusDays(5),
                LocalDateTime.now().plusDays(10).withNano(0), BookingStatus.APPROVED);

        bookingInfoDto = BookingMapper.toBookingInfoDto(booking);

        when(bookingService.getAllBookingsOwner(any(Long.class), any(String.class), any(Integer.class),
                any(Integer.class))).thenReturn(List.of(booking));

        mvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .param("state", "test")
                        .param("from", "0")
                        .param("size", "2")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(bookingInfoDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].item.id", is(bookingInfoDto.getItem().getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingInfoDto.getStart())))
                .andExpect(jsonPath("$[0].end", is(bookingInfoDto.getEnd())))
                .andExpect(jsonPath("$[0].status", is(bookingInfoDto.getStatus().name())));
    }
}
