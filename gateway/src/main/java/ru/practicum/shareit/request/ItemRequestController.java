package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long requestorId,
                                         @Valid @RequestBody ItemRequestDto itemRequestDto) {
        log.info("Create userId= {} request {}", requestorId, itemRequestDto);
        return itemRequestClient.create(requestorId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllByRequestorId(@RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Get all requests by requestorId={}", requestorId);
        return itemRequestClient.findAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAll(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PositiveOrZero @RequestParam(value = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(value = "size", defaultValue = "10") Integer size) {
        log.info("Get all request userId={} from={} size={}",userId, from, size);
        return itemRequestClient.findAll(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                        @PathVariable("requestId") Long requestId) {
        log.info("Get by requestId={} userId={}", userId, requestId);
        return itemRequestClient.findById(userId, requestId);
    }
}
