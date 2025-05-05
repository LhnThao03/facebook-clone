package com.example.facebook_clone.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Base64;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final String BASE_UPLOAD_DIR = "IMG";

    public String saveFile(MultipartFile file, String subFolder) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        try {
            // Tạo thư mục gốc
            Path basePath = Paths.get(BASE_UPLOAD_DIR);
            Files.createDirectories(basePath);

            // Tạo thư mục con nếu cần
            Path uploadPath = (subFolder != null && !subFolder.isEmpty())
                    ? basePath.resolve(subFolder)
                    : basePath;
            Files.createDirectories(uploadPath);

            // Dùng đúng tên file gốc
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isBlank()) {
                throw new IllegalArgumentException("Invalid file name");
            }

            Path filePath = uploadPath.resolve(originalFilename);

            // Ghi đè file nếu trùng tên
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Trả về đường dẫn cho DB
            if (subFolder != null && !subFolder.isEmpty()) {
                return "/" + BASE_UPLOAD_DIR + "/" + subFolder + "/" + originalFilename;
            } else {
                return "/" + BASE_UPLOAD_DIR + "/" + originalFilename;
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to store file: " + e.getMessage());
        }
    }
    
    
}
