package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class ItemDtoJsonTest {

    @Autowired
    private JacksonTester<ItemDto> json;

    @Autowired JacksonTester<ItemInfoDto> jsonInfo;

    @Test
    void testSerialize() throws Exception {
        var mockUser = new User(1L, "user", "user@mail.ru");
        var mockItemRequest = new ItemRequest(1L, "test", mockUser,
                LocalDateTime.now().minusDays(50), new HashSet<>());

        var mockItem = new Item(1L, "Дрель", "test",
                Boolean.TRUE, mockUser, mockItemRequest, null, null, new HashSet<>());

        var mockComment = new Comment(1L, "Дрель", mockItem,
                mockUser, LocalDateTime.now());

        var mockBooking = new Booking(1L, mockItem, mockUser, LocalDateTime.now().minusDays(20),
                LocalDateTime.now().minusDays(10), BookingStatus.APPROVED);

        mockItem.setLastBooking(mockBooking);
        mockItem.setComments(Set.of(mockComment));

        var dto = ItemMapper.toItemDto(mockItem);
        var dtoInfo = ItemMapper.toItemInfoDto(mockItem);

        var result = json.write(dto);
        var resultInfo = jsonInfo.write(dtoInfo);

        assertThat(resultInfo).hasJsonPath("$.id");
        assertThat(resultInfo).hasJsonPath("$.name");
        assertThat(resultInfo).hasJsonPath("$.description");
        assertThat(resultInfo).hasJsonPath("$.available");
        assertThat(resultInfo).hasJsonPath("$.requestId");

        assertThat(resultInfo).extractingJsonPathNumberValue("$.id").isEqualTo(dtoInfo.getId().intValue());
        assertThat(resultInfo).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(resultInfo).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(resultInfo).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(resultInfo).extractingJsonPathNumberValue("$.requestId")
                .isEqualTo(dto.getRequestId().intValue());


        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.description");
        assertThat(result).hasJsonPath("$.available");
        assertThat(result).hasJsonPath("$.nextBooking");
        assertThat(result).hasJsonPath("$.lastBooking");
        assertThat(result).hasJsonPath("$.comments");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.description").isEqualTo(dto.getDescription());
        assertThat(result).extractingJsonPathBooleanValue("$.available").isEqualTo(dto.getAvailable());
        assertThat(result).extractingJsonPathNumberValue("$.lastBooking.id")
                .isEqualTo(dto.getLastBooking().getId().intValue());
        assertThat(result).extractingJsonPathArrayValue("$.comments").hasSize(1);
    }
}
