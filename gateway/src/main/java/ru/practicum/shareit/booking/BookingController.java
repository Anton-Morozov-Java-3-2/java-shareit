package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.booking.dto.ValidateOnCreateBooking;
import ru.practicum.shareit.item.dto.ValidateOnUpdateItem;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
	private final BookingClient bookingClient;

	@PostMapping
	@Validated(ValidateOnCreateBooking.class)
	public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") Long bookerId,
										 @Valid @RequestBody BookingDto bookingDto) {
		log.info("Create booking {}, bookerId={}", bookingDto, bookerId);
		return bookingClient.create(bookerId, bookingDto);
	}

	@PatchMapping("/{bookingId}")
	@Validated(ValidateOnUpdateItem.class)
	public ResponseEntity<Object> updateStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
											   @PathVariable Long bookingId,
											   @RequestParam("approved") Boolean approved) {
		log.info("Update booking status={} bookingId={}, ownerId={}", approved, bookingId, ownerId);
		return bookingClient.updateStatus(ownerId, bookingId, approved);
	}

	@GetMapping("/{bookingId}")
	public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long requesterId,
												 @PathVariable Long bookingId) {
		log.info("Get bookingId {}, userId={}", bookingId, requesterId);
		return bookingClient.getBookingById(requesterId, bookingId);
	}

	@GetMapping
	public ResponseEntity<Object> getAllBookingUser(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "ALL") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getAllBookingsUser(userId, state, from, size);
	}

	@GetMapping("/owner")
	public ResponseEntity<Object> getAllBookingOwner(@RequestHeader("X-Sharer-User-Id") long userId,
			@RequestParam(name = "state", defaultValue = "all") String stateParam,
			@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
			@Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
		BookingState state = BookingState.from(stateParam)
				.orElseThrow(() -> new IllegalArgumentException("Unknown state: " + stateParam));
		log.info("Get booking with state {}, userId={}, from={}, size={}", stateParam, userId, from, size);
		return bookingClient.getAllBookingsOwner(userId, state, from, size);
	}
}
