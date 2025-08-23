package com.codegym.demo.dto.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneValidator implements ConstraintValidator<UniquePhone, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.trim().isEmpty()) {
            return true; // đã có @NotBlank check riêng rồi
        }
        return !userRepository.existsByPhone(phone);
    }
}
