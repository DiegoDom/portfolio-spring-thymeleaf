package com.portfolio.my_portfolio.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

  @Value("${file.upload.dir}")
  private String uploadDir;

  public String storeFile(MultipartFile file) throws IOException {
    if (file.isEmpty()) {
      throw new IOException("The uploaded file it's empty");
    }

    String ogFilename = file.getOriginalFilename();
    String extension = "";

    if (ogFilename != null) {
      int dotIndex = ogFilename.lastIndexOf('.');
      if (dotIndex > 0) {
        extension = ogFilename.substring(dotIndex);
      }
    }

    String fileName = UUID.randomUUID().toString() + extension;

    Path filePath = Paths.get(uploadDir, fileName).normalize();

    Files.copy(file.getInputStream(), filePath);

    return "/img/" + fileName;
  }
}
