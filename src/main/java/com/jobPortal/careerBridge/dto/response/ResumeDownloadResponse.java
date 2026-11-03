package com.jobPortal.careerBridge.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResumeDownloadResponse {

    private byte[] file;
    private String fileName;
    private String contentType;
}
