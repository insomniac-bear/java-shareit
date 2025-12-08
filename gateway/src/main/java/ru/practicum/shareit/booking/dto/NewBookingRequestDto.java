package ru.practicum.shareit.booking.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@StartBeforeEnd
public class NewBookingRequestDto {
    @NotNull
    private final Long itemId;

    @NotNull
    @FutureOrPresent
    private final LocalDateTime start;

    @NotNull
    @Future
    private final LocalDateTime end;
}
