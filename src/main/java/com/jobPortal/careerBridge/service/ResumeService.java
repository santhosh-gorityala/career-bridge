package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.response.ResumeDownloadResponse;
import com.jobPortal.careerBridge.dto.response.ResumeResponse;
import com.jobPortal.careerBridge.entity.Resume;
import com.jobPortal.careerBridge.entity.UserProfile;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.exception.ResumeAlreadyExistsException;
import com.jobPortal.careerBridge.repository.ResumeRepository;
import com.jobPortal.careerBridge.repository.UserProfileRepository;
import com.jobPortal.careerBridge.service.storage.FileStorageService;
import com.jobPortal.careerBridge.service.storage.ResumeFileValidator;
import com.jobPortal.careerBridge.service.storage.S3FileStorageService;
import com.jobPortal.careerBridge.service.storage.StorageKeyGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
public class ResumeService {
    private final CurrentUserService currentUserService;
    private final UserProfileRepository userProfileRepository;
    private final ResumeRepository resumeRepository;
    private final ResumeFileValidator resumeFileValidator;
    private final StorageKeyGenerator storageKeyGenerator;
    private final FileStorageService fileStorageService;

    public ResumeService(
            CurrentUserService currentUserService,
            UserProfileRepository userProfileRepository,
            ResumeRepository resumeRepository,
            ResumeFileValidator resumeFileValidator,
            StorageKeyGenerator storageKeyGenerator,
            FileStorageService fileStorageService
    ){
        this.currentUserService = currentUserService;
        this.userProfileRepository = userProfileRepository;
        this.resumeRepository = resumeRepository;
        this.resumeFileValidator = resumeFileValidator;
        this.storageKeyGenerator = storageKeyGenerator;
        this.fileStorageService = fileStorageService;
    }

    @Transactional
    public ResumeResponse upload(MultipartFile file){

        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).orElseThrow(
                        ()-> new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        if(resumeRepository.
                findByProfileId(userProfile.getId()).
                isPresent()){
            throw new ResumeAlreadyExistsException(
                    "Resume already exists. Use replace instead"
            );
        }

        resumeFileValidator.validate(file);
        String storageKey =
                storageKeyGenerator.generateResumeKey(
                file.getOriginalFilename()
        );

        fileStorageService.upload(
                file,
                storageKey
        );

        LocalDateTime now = LocalDateTime.now();

        Resume resume =
                new Resume(
                  userProfile,
                  file.getOriginalFilename(),
                  storageKey,
                  file.getContentType(),
                  file.getSize(),
                  now,
                  now
                );
        Resume savedResume =
                resumeRepository.save(resume);


        return new ResumeResponse(
                resume.getId(),
                resume.getFileName(),
                resume.getContentType(),
                resume.getFileSize(),
                resume.getCreatedAt(),
                resume.getUpdateAt()
        );
    }

    @Transactional(readOnly = true)
    public ResumeResponse getMyResume(){
        Long userId = currentUserService.getCurrentUserId();

        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        Resume resume = resumeRepository.
                findByProfileId(userProfile.getId()).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        return new ResumeResponse(
          resume.getId(),
          resume.getFileName(),
          resume.getContentType(),
          resume.getFileSize(),
          resume.getCreatedAt(),
          resume.getUpdateAt()
        );


    }

    @Transactional(readOnly = true)
    public ResumeDownloadResponse downloadResume(){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );
        Resume resume = resumeRepository.
                findByProfileId(userProfile.getId()).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        String storageKey = resume.getStorageKey();
        byte[] resumeFile =
                fileStorageService.download(storageKey);

        return new ResumeDownloadResponse(
          resumeFile,
          resume.getFileName(),
          resume.getContentType()
        );
    }

    @Transactional
    public ResumeResponse replaceResume(MultipartFile file){
        Long userId = currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        Resume resume = resumeRepository.
                findByProfileId(userProfile.getId()).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        resumeFileValidator.validate(file);
        String oldStorageKey =
                resume.getStorageKey();
        String newStorageKey =
                storageKeyGenerator.generateResumeKey(
                        file.getOriginalFilename()
                );
        fileStorageService.upload(
                file,
                newStorageKey
        );

        resume.updateFile(
                file.getOriginalFilename(),
                newStorageKey,
                file.getContentType(),
                file.getSize()
        );
        Resume updatedResume =
                resumeRepository.save(resume);
        fileStorageService.delete(oldStorageKey);
        return new ResumeResponse(
          updatedResume.getId(),
          updatedResume.getFileName(),
          updatedResume.getContentType(),
          updatedResume.getFileSize(),
          updatedResume.getCreatedAt(),
          updatedResume.getUpdateAt()
        );
    }

    @Transactional
    public void deleteResume(){
        Long userId =
                currentUserService.getCurrentUserId();
        UserProfile userProfile = userProfileRepository.
                findByUserId(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Profile not found"
                        )
                );

        Resume resume = resumeRepository.
                findByProfileId(userProfile.getId()).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Resume not found"
                        )
                );

        String storageKey = resume.getStorageKey();
        fileStorageService.delete(storageKey);
        resumeRepository.delete(resume);
    }


}
