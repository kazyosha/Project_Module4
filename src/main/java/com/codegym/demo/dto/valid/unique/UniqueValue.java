package com.codegym.demo.dto.valid.unique;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueValidator.class) // class xử lý logic
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueValue {
    String message() default "Giá trị đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String field();
}
