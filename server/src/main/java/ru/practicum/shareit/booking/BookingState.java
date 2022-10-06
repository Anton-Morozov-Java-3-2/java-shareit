package ru.practicum.shareit.booking;

public enum BookingState {
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED, UNSUPPORTED;

    public static BookingState  getBookingState(String state) {
        for (BookingState bookingState : BookingState.values()) {
            if (state.equals(bookingState.toString())) {
                return bookingState;
            }
        }
        return  BookingState.UNSUPPORTED;
    }
}
