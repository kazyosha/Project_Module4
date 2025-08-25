package com.codegym.demo.dto.valid.unique;

import com.codegym.demo.repositories.IUserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniqueValidator implements ConstraintValidator<UniqueValue, String> {

    @Autowired
    private IUserRepository iUserRepository;

    private String field;

    @Override
    public void initialize(UniqueValue constraintAnnotation) {
        this.field = constraintAnnotation.field(); // lấy tên field cần check
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.trim().isEmpty()) {
            return true;
        }

        switch (field) {
            case "phone":
                return !iUserRepository.existsByPhone(value);
            case "email":
                return !iUserRepository.existsByEmail(value);
            default:
                throw new IllegalArgumentException("Field không hỗ trợ: " + field);
        }
    }
}
