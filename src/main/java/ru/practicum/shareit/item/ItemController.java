package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.CommentNotAvailableException;
import ru.practicum.shareit.exception.ItemAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{itemId}/comment")
    @Validated(ValidateOnCreateItem.class)
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @Valid @RequestBody CommentDto commentDto, @PathVariable Long itemId)
            throws UserNotFoundException, CommentNotAvailableException, ItemNotFoundException {
        return CommentMapper.toCommentDto(itemService.postComment(userId, itemId, CommentMapper.toComment(commentDto)));
    }

    @PostMapping
    @Validated(ValidateOnCreateItem.class)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto)
            throws UserNotFoundException {
        return ItemMapper.toItemDto(itemService.create(ownerId, ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidateOnUpdateItem.class)
    public ItemDto update(@PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto)
            throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        return ItemMapper.toItemDto(itemService.update(ownerId, itemId,ItemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId)
            throws ItemNotFoundException, UserNotFoundException {
        return ItemMapper.toItemDto(itemService.findItemById(itemId, userId));
    }

    @GetMapping
    public List<ItemDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws UserNotFoundException {
        return itemService.readAllItemsOwner(ownerId).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text, @RequestHeader("X-Sharer-User-Id") Long ownerId)
            throws UserNotFoundException {
        return itemService.searchItem(ownerId, text).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
