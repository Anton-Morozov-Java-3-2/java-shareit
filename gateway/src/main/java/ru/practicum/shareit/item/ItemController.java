package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ValidateOnCreateItem;
import ru.practicum.shareit.item.dto.ValidateOnUpdateItem;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping("/{itemId}/comment")
    @Validated(ValidateOnCreateItem.class)
    public ResponseEntity<Object> postComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @Valid @RequestBody CommentDto commentDto,
                                              @PathVariable Long itemId) {
        log.info("Post comment {} userId= {} itemId= {}", commentDto, userId, itemId);
        return itemClient.postComment(userId, itemId, commentDto);
    }

    @PostMapping
    @Validated(ValidateOnCreateItem.class)
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Create item {} ownerId= {}", itemDto, ownerId);
        return itemClient.create(ownerId, itemDto);
    }

    @PatchMapping("/{itemId}")
    @Validated(ValidateOnUpdateItem.class)
    public ResponseEntity<Object> update(@PathVariable("itemId") Long itemId,
                          @RequestHeader("X-Sharer-User-Id") Long ownerId,
                          @Valid @RequestBody ItemDto itemDto) {
        log.info("Update ownerId= {} itemId= {} item {}", ownerId, itemId, itemDto);
        return itemClient.update(ownerId, itemId,itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> get(@PathVariable("itemId") Long itemId,
                                      @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get userId= {} itemId= {}", userId, itemId);
        return itemClient.findItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                @PositiveOrZero @RequestParam(value = "from", defaultValue = "0")
                                                Integer from,
                                                @Positive @RequestParam(value = "size", defaultValue = "10")
                                                Integer size) {
        log.info("Get all item ownerId= {} from= {} size= {}", ownerId, from, size);
        return itemClient.readAllItemsOwner(ownerId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(@RequestParam("text") String text,
                                     @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                     @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                     @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get item contain text= {} userId={} form= {} size= {}",text, ownerId, from, size);
        return itemClient.searchItem(ownerId, text, from, size);
    }
}
