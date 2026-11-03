package com.jobPortal.careerBridge.service.storage;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    String upload(
            MultipartFile file,
            String storageKey
    );

    byte[] download(
            String storageKey
    );

    void delete(
            String storageKey
    );

}
