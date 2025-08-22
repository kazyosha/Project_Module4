package com.codegym.demo.untils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileManager {
    public void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            boolean deleted = file.delete();
            if (!deleted) {
                System.out.println("Failed to delete file: " + filePath);
            }
        } else {
            System.out.println("File not found: " + filePath);
        }
    }

    public String uploadFile(String uploadDirFile, MultipartFile file) {
        try {
            try {
                if (file == null || file.isEmpty()) {
                    throw new RuntimeException("File is empty");
                }

                // Lấy tên gốc và phần mở rộng
                String originalName = file.getOriginalFilename();
                String ext = "";
                if (originalName != null && originalName.contains(".")) {
                    ext = originalName.substring(originalName.lastIndexOf(".")); // ví dụ ".jpg"
                }

                // Sinh tên file duy nhất
                String fileName = UUID.randomUUID() + ext;

                // Tạo thư mục nếu chưa tồn tại
                File uploadDir = new File(uploadDirFile);
                if (!uploadDir.exists()) {
                    if (!uploadDir.mkdirs()) {
                        throw new RuntimeException("Cannot create upload dir: " + uploadDirFile);
                    }
                }

                File dest = new File(uploadDir, fileName);
                file.transferTo(dest);

                return fileName;

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("File upload failed: " + e.getMessage(), e);
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}
