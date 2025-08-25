package com.codegym.demo.dto.valid.customImage;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidImage {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
