package com.jobPortal.careerBridge.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
public class S3FileStorageService implements FileStorageService{
    private final S3Client s3Client;
    private final String bucketName;

    public S3FileStorageService(
            S3Client s3Client,
            @Value("${aws.s3.bucket}") String bucketName) {

        this.s3Client = s3Client;
        this.bucketName = bucketName;
    }

    @Override
    public String upload(
            MultipartFile file,
            String storageKey) {

        try {
            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(storageKey)
                            .contentType(file.getContentType())
                            .contentLength(file.getSize())
                            .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromInputStream(
                            file.getInputStream(),
                            file.getSize()
                    )
            );

            return storageKey;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to upload file to S3",
                    e
            );
        }
    }

    @Override
    public byte[] download(String storageKey) {

        GetObjectRequest request =
                GetObjectRequest.builder()
                        .bucket(bucketName)
                        .key(storageKey)
                        .build();

        return s3Client.getObjectAsBytes(request)
                .asByteArray();
    }

    @Override
    public void delete(String storageKey) {

        DeleteObjectRequest request =
                DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(storageKey)
                        .build();

        s3Client.deleteObject(request);
    }
}
