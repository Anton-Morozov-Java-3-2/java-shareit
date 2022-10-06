package ru.practicum.shareit.request;

import ru.practicum.shareit.item.ItemMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.stream.Collectors;

public class ItemRequestMapper {

    public static final DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public static ItemRequestDto itemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto(itemRequest.getId(),
                itemRequest.getDescription(),
                itemRequest.getCreated().format(format),
                itemRequest.getItems().stream().map(ItemMapper::toItemInfoDto).collect(Collectors.toList()));
    }

    public static ItemRequest toItemRequest(ItemRequestDto itemRequestDto) {
        return new ItemRequest(itemRequestDto.getId(),
                itemRequestDto.getDescription(),
                null, itemRequestDto.getCreated() != null ?
                LocalDateTime.parse(itemRequestDto.getCreated(), format) : null, new HashSet<>());
    }
}
