package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDto create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                 @RequestBody ItemRequestDto itemRequestDto)
            throws UserNotFoundException {
        return itemRequestService.create(requestorId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDto> getAllByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId)
            throws UserNotFoundException {
        return itemRequestService.findAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(value = "from", required = false) Integer from,
                                       @RequestParam(value = "size", required = false) Integer size)
            throws UserNotFoundException {
        return itemRequestService.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("requestId") Long requestId)
            throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException {
        return itemRequestService.findById(userId, requestId);
    }
}
