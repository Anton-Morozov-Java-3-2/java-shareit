package ru.practicum.shareit.item;

import lombok.Value;

@Value
public class ItemInfoDto {
    Long id;
    String name;
    String description;
    Boolean available;
    Long requestId;
}
