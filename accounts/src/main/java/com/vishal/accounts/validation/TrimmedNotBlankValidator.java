package com.vishal.accounts.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TrimmedNotBlankValidator  implements ConstraintValidator<TrimmedNotBlank, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;

        String trimmed = value.trim();
        return !trimmed.isBlank() && trimmed.length() >= 3 && trimmed.length() <= 30;
    }
}
