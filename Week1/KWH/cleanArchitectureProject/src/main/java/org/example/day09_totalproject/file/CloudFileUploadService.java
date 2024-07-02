package org.example.day09_totalproject.file;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class CloudFileUploadService implements FileUploadService{
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Value("${project.upload.s3path}")
    private String s3Path;

    private final AmazonS3 amazonS3;

    public CloudFileUploadService(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    @Override
    public String upload(MultipartFile file) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        String fileName = makeFileName(file);

        try {
            amazonS3.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return s3Path+"/"+fileName;
    }

    public String makeFileName(MultipartFile file) {
        String data = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        String originalName = file.getOriginalFilename();

        return data + "/" + UUID.randomUUID()+ "_" +originalName;
    }
}
