package com.vishal.accounts.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TrimmedNotBlankValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TrimmedNotBlank {
    String message() default "Field must not be blank or contain only whitespace";
    Class<?> [] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
