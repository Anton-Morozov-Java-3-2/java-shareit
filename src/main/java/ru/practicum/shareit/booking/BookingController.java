package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.ValidateOnUpdateItem;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @Validated(ValidateOnCreateBooking.class)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") Long bookerId, @Valid @RequestBody BookingDto bookingDto)
            throws UserNotFoundException, ItemNotFoundException,
            BookingDateNotValidException, ItemNotAvailableException, BookingAccessException {
        return  BookingMapper.toBookingDto(bookingService.create(bookerId, BookingMapper.toBooking(bookingDto)));
    }

    @PatchMapping("/{bookingId}")
    @Validated(ValidateOnUpdateItem.class)
    public BookingInfoDto updateStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                   @PathVariable Long bookingId,
                                   @RequestParam("approved") Boolean approved) throws UserNotFoundException,
            BookingNotFoundException, ItemAccessException, ItemChangeStatusException {
        return BookingMapper.toBookingInfoDto(bookingService.updateStatus(ownerId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public BookingInfoDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long requesterId, @PathVariable Long bookingId)
            throws UserNotFoundException, BookingNotFoundException, BookingAccessException {
        return BookingMapper.toBookingInfoDto(bookingService.getBookingById(requesterId, bookingId));
    }

    @GetMapping
    public List<BookingInfoDto> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", required = false) @Min(0) Integer from,
                                                  @RequestParam(value = "size", required = false) @Min(1) Integer size)

            throws UserNotFoundException, InvalidParamException {
        return bookingService.getAllBookingsUser(requesterId, state, from, size)
                .stream()
                .map(BookingMapper::toBookingInfoDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public List<BookingInfoDto> getAllBookingOwner(@RequestHeader("X-Sharer-User-Id") Long requesterId,
                                                  @RequestParam(value = "state", defaultValue = "ALL") String state,
                                                  @RequestParam(value = "from", required = false) @Min(0) Integer from,
                                                  @RequestParam(value = "size", required = false) @Min(1) Integer size)
            throws UserNotFoundException, InvalidParamException {
        return bookingService.getAllBookingsOwner(requesterId, state, from, size)
                .stream()
                .map(BookingMapper::toBookingInfoDto)
                .collect(Collectors.toList());
    }
}
