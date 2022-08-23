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
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long idOwner,
                          @Valid @RequestBody ItemDto itemDto)
            throws UserNotFoundException {
        return DtoMapper.toItemDto(itemService.create(idOwner, DtoMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidateOnUpdateItem.class)
    public ItemDto update(@PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long idOwner,
                          @Valid @RequestBody ItemDto itemDto)
            throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        return DtoMapper.toItemDto(itemService.update(idOwner, itemId,DtoMapper.toItem(itemDto)));
    }

    @GetMapping("/{idItem}")
    public ItemDto get(@PathVariable("idItem") Long id, @RequestHeader("X-Sharer-User-Id") Long idUser)
            throws ItemNotFoundException {
        return DtoMapper.toItemDto(itemService.read(id));
    }

    @GetMapping
    public List<ItemDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") Long idOwner)
            throws UserNotFoundException {
        return itemService.readAllItemsOwner(idOwner).stream().map(DtoMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text, @RequestHeader("X-Sharer-User-Id") Long idOwner)
            throws UserNotFoundException {
        return itemService.searchItem(idOwner, text).stream().map(DtoMapper::toItemDto).collect(Collectors.toList());
    }
}
