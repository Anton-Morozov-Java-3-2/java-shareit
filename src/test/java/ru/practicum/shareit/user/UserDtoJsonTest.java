package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
public class UserDtoJsonTest {

    @Autowired
    private JacksonTester<UserDto> json;

    @Autowired JacksonTester<UserInfoDto> jsonInfo;

    @Test
    void testSerialize() throws Exception {
        var dto = new UserDto(1L, "test", "2022-09-12T21:00:01");
        var dtoInfo = new UserInfoDto(1L);
        var result = json.write(dto);
        var resultInfo = jsonInfo.write(dtoInfo);
        assertThat(resultInfo).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.id");
        assertThat(result).hasJsonPath("$.name");
        assertThat(result).hasJsonPath("$.email");

        assertThat(resultInfo).extractingJsonPathNumberValue("$.id").isEqualTo(dtoInfo.getId().intValue());
        assertThat(result).extractingJsonPathNumberValue("$.id").isEqualTo(dto.getId().intValue());
        assertThat(result).extractingJsonPathStringValue("$.name").isEqualTo(dto.getName());
        assertThat(result).extractingJsonPathStringValue("$.email").isEqualTo(dto.getEmail());
    }
}
