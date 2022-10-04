package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping("/{itemId}/comment")
    public CommentDto postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @RequestBody CommentDto commentDto,
                                  @PathVariable Long itemId)
            throws UserNotFoundException, CommentNotAvailableException, ItemNotFoundException {
        return CommentMapper.toCommentDto(itemService.postComment(userId, itemId, CommentMapper.toComment(commentDto)));
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @RequestBody ItemDto itemDto)
            throws UserNotFoundException, ItemRequestNotFoundException {
        return ItemMapper.toItemDto(itemService.create(ownerId, ItemMapper.toItem(itemDto)));
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @RequestBody ItemDto itemDto)
            throws ItemNotFoundException, UserNotFoundException, ItemAccessException {
        return ItemMapper.toItemDto(itemService.update(ownerId, itemId,ItemMapper.toItem(itemDto)));
    }

    @GetMapping("/{itemId}")
    public ItemDto get(@PathVariable("itemId") Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId)
            throws ItemNotFoundException, UserNotFoundException {
        return ItemMapper.toItemDto(itemService.findItemById(itemId, userId));
    }

    @GetMapping
    public List<ItemDto> getItemsOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                       @RequestParam(value = "from", required = false) Integer from,
                                       @RequestParam(value = "size", required = false) Integer size)
            throws UserNotFoundException {
        return itemService.readAllItemsOwner(ownerId, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam("text") String text,
                                     @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @RequestParam(value = "from", required = false) Integer from,
                                     @RequestParam(value = "size", required = false) Integer size)
            throws UserNotFoundException {
        return itemService.searchItem(ownerId, text, from, size).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
    }
}
