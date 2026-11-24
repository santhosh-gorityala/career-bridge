package com.jobPortal.careerBridge.service.storage;


import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ApplicationStorageKeyGenerator {

    public String generate(
            Long applicantId,
            String originalFilename
    ){
        String extension = "";

        int dotIndex = originalFilename.lastIndexOf(".");

        if(dotIndex >= 0){
            extension = originalFilename.substring(dotIndex);
        }
        return "applications/"
                +applicantId
                +"/"
                + UUID.randomUUID()
                +extension;

    }

}
