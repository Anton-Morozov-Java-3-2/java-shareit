package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.request.ItemRequest;

import java.util.HashSet;
import java.util.stream.Collectors;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                item.getNextBooking() == null ? null : BookingMapper.toBookingItemInfoDto(item.getNextBooking()),
                item.getLastBooking() == null ? null : BookingMapper.toBookingItemInfoDto(item.getLastBooking()),
                item.getComments() == null ? null :
                        item.getComments().stream()
                                .map(CommentMapper::toCommentDto)
                                .collect(Collectors.toList()));
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(), null, itemDto.getRequestId() == null ? null :
                new ItemRequest(itemDto.getRequestId()),
                null, null, new HashSet<>());
    }

    public static ItemInfoDto toItemInfoDto(Item item) {
        return new ItemInfoDto(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getIsAvailable(),
                item.getRequest().getId());
    }
}
