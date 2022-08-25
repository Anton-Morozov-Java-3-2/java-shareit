package ru.practicum.shareit.request.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequestDto {
    private Long id;
    private final String description;
    private Long requestorId;
    private LocalDateTime created;
}