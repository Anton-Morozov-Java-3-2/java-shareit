package ru.practicum.shareit.request;

import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto create(Long requestorId, ItemRequestDto itemRequestDto) throws UserNotFoundException;

    List<ItemRequestDto> findAllByRequestorId(Long requestorId) throws UserNotFoundException;

    List<ItemRequestDto> findAll(Long requestorId, Integer from, Integer size)
            throws UserNotFoundException;

    ItemRequestDto findById(Long userId, Long requestId)
            throws UserNotFoundException, ItemNotFoundException, ItemRequestNotFoundException;
}
