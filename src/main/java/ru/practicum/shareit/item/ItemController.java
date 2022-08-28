package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ValidateOnCreateItem;
import ru.practicum.shareit.item.dto.ValidateOnUpdateItem;
import ru.practicum.shareit.mapper.DtoMapper;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    @Validated(ValidateOnCreateItem.class)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto)
            throws UserNotFoundException {
        return DtoMapper.toItemDto(itemService.create(ownerId, DtoMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidateOnUpdateItem.class)
    public ItemDto update(@PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto)
            throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        return DtoMapper.toItemDto(itemService.update(ownerId, itemId,DtoMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable("itemId") Long id, @RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws ItemNotFoundException {
        return DtoMapper.toItemDto(itemService.findItemById(id));
    }

    @GetMapping
    public List<ItemDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws UserNotFoundException {
        return itemService.readAllItemsOwner(ownerId).stream().map(DtoMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text, @RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws UserNotFoundException {
        return itemService.searchItem(ownerId, text).stream().map(DtoMapper::toItemDto).collect(Collectors.toList());
    }
}
