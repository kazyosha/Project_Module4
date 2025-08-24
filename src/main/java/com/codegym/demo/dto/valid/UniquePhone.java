package com.codegym.demo.dto.valid;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniquePhoneValidator.class) // class xử lý logic
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UniquePhone {
    String message() default "Số điện thoại đã tồn tại";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
