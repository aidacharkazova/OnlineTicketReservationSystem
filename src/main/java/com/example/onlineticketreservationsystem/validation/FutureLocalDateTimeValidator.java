package com.example.onlineticketreservationsystem.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDateTime;

public class FutureLocalDateTimeValidator implements ConstraintValidator<FutureLocalDateTime, LocalDateTime> {
    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) return true; // use @NotNull separately
        return value.isAfter(LocalDateTime.now());
    }
}
