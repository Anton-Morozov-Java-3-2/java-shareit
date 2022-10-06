package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.ItemInfoDto;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    private String created;
    private List<ItemInfoDto> items = new ArrayList<>();

    public ItemRequestDto(Long id, String description, String created) {
        this.setId(id);
        this.setDescription(description);
        this.setCreated(created);
    }

    public ItemRequestDto() {
    }
}