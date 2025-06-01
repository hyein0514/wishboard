package com.guesthouse.wishboard.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class ImageUploadService {
    private final String uploadDir = "C:/upload";  // WebConfig, Controller와 동일

    public String upload(MultipartFile file) throws IOException {
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        String name = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dest = dir.toPath().resolve(name);
        file.transferTo(dest);
        return "/uploaded/" + name;
    }
}

