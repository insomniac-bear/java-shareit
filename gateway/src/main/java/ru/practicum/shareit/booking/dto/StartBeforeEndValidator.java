package ru.practicum.shareit.booking.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, NewBookingRequestDto> {
    @Override
    public boolean isValid(NewBookingRequestDto dto, ConstraintValidatorContext context) {
        if (dto.getStart() == null || dto.getEnd() == null) {
            return true;
        }

        return !dto.getStart().equals(dto.getEnd()) && dto.getStart().isBefore(dto.getEnd());
    }
}
