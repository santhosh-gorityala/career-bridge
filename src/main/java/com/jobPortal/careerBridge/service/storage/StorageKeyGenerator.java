package com.jobPortal.careerBridge.service.storage;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class StorageKeyGenerator {
    public String generateResumeKey(String originalFileName) {

        String extension = "";

        if (originalFileName != null &&
                originalFileName.contains(".")) {

            extension = originalFileName.substring(
                    originalFileName.lastIndexOf(".")
            );
        }

        return "resumes/" +
                UUID.randomUUID() +
                extension;
    }
}
