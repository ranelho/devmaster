package com.devmaster.cliente.application.api.annotation.constraints;

import com.devmaster.cliente.application.api.annotation.Adult;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDate now = LocalDate.now();
        LocalDate minimumAge = now.minusYears(18);
        return value.isBefore(minimumAge) || value.isEqual(minimumAge);
    }
}
