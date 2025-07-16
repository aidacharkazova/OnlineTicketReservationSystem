package com.example.onlineticketreservationsystem.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureLocalDateTimeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureLocalDateTime {
    String message() default "Date/time must be in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

