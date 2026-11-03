package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.response.ResumeDownloadResponse;
import com.jobPortal.careerBridge.dto.response.ResumeResponse;
import com.jobPortal.careerBridge.service.ResumeService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/profile/resume")
public class UserResumeController {

    private final ResumeService resumeService;

    public UserResumeController(ResumeService resumeService){
        this.resumeService = resumeService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponse> uploadResume(
            @RequestParam("file") MultipartFile file
            ){

        ResumeResponse resumeResponse =
                resumeService.upload(file);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(resumeResponse);

    }

    @GetMapping
    public ResponseEntity<ResumeResponse> getResume(){
        ResumeResponse resumeResponse =
                resumeService.getMyResume();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(resumeResponse);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadResume(){
        ResumeDownloadResponse resumeResponse =
                resumeService.downloadResume();
        return ResponseEntity.
                status(HttpStatus.OK).
                header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\""+
                                resumeResponse.getFileName()+
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(
                                resumeResponse.getContentType()
                        )
                )
                .body(resumeResponse.getFile());
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ResumeResponse> replaceResume(
            @RequestParam("file") MultipartFile file
    ){
        ResumeResponse resumeResponse =
                resumeService.replaceResume(file);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(resumeResponse);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteResume(){
        resumeService.deleteResume();
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).
                build();
    }



}
