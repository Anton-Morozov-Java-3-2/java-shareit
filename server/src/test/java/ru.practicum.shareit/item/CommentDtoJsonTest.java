package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class CommentDtoJsonTest {

    @Autowired
    private JacksonTester<CommentDto> json;

    @Test
    void testSerialize() throws Exception {

        var mockUser = new User(1L, "user", "user@mail.ru");

        var mockItemRequest = new ItemRequest(1L, "test", mockUser,
                LocalDateTime.now().minusDays(50), new HashSet<>());

        var mockItem = new Item(1L, "Дрель", "test",
                Boolean.TRUE, mockUser, mockItemRequest, null, null, new HashSet<>());

        var mockComment = new Comment(1L, "text", mockItem, mockUser, LocalDateTime.now());

        mockItem.setComments(Set.of(mockComment));

        var dto = CommentMapper.toCommentDto(mockComment);

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.text");
        assertThat(result).hasJsonPath("$.authorName");
        assertThat(result).hasJsonPath("$.created");

        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
        assertThat(result).extractingJsonPathStringValue("$.authorName").isEqualTo(dto.getAuthorName());
        assertThat(result).extractingJsonPathStringValue("$.created").isEqualTo(dto.getCreated());
    }
}
