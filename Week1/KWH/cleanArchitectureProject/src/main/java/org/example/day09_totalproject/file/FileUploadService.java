package org.example.day09_totalproject.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    public String upload(MultipartFile file);
}
