package com.codegym.demo.dto.valid.customImage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;


@Component
public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private final List<String> allowedExtensions = Arrays.asList("png", "jpeg", "jpg", "gif");
    private final long MAX_SIZE = 2 * 1024 * 1024; // 2MB

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true;
        }

        if (file.getSize() > MAX_SIZE) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File quá lớn, tối đa 2MB"
            ).addConstraintViolation();
            return false;
        }

        String filename = file.getOriginalFilename();
        if (filename == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "File không có tên hợp lệ"
            ).addConstraintViolation();
            return false;
        }

        String extension = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
        if (!allowedExtensions.contains(extension)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Đuôi file không hợp lệ, chỉ chấp nhận: png, jpg, jpeg, gif"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
