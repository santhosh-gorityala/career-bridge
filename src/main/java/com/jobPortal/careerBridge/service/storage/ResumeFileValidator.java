package com.jobPortal.careerBridge.service.storage;

import com.jobPortal.careerBridge.exception.InvalidResumeFileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class ResumeFileValidator {

    private final static long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final static Set<String> ALLOWED_CONTENT_TYPE =
            Set.of(
                    "application/pdf",
                    "application/msword",
                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
            );

    public void validate(MultipartFile file){

        if( file == null || file.isEmpty() ){
            throw new InvalidResumeFileException(
              "Resume file cannot be empty"
            );
        }

        if(file.getSize() > MAX_FILE_SIZE){
            throw new InvalidResumeFileException(
                    "Resume file size cannot exceed 5 MB"
            );
        }

        String contentType = file.getContentType();
        if(contentType == null ||
                !ALLOWED_CONTENT_TYPE.contains(contentType)){
            throw new InvalidResumeFileException(
                    "Only PDF, DOC, and DOCX files are allowed"
            );
        }


    }



}
