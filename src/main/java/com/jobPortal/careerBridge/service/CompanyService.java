package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.CompanyRequest;
import com.jobPortal.careerBridge.dto.response.CompanyResponse;
import com.jobPortal.careerBridge.entity.Company;
import com.jobPortal.careerBridge.entity.User;
import com.jobPortal.careerBridge.exception.ResourceNotFoundException;
import com.jobPortal.careerBridge.repository.CompanyRepository;
import com.jobPortal.careerBridge.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CurrentUserService currentUserService;
    private final UserRepository userRepository;
    @Autowired
    public CompanyService(
            CompanyRepository companyRepository,
            CurrentUserService currentUserService,
            UserRepository userRepository
    ){
        this.companyRepository = companyRepository;
        this.currentUserService = currentUserService;
        this.userRepository = userRepository;
    }

    @Transactional
    public CompanyResponse createCompany(
            CompanyRequest companyRequest
    ){
        Long userId = currentUserService.getCurrentUserId();
        User owner = userRepository.findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        LocalDateTime now = LocalDateTime.now();

        Company company = new Company(
          owner,
          companyRequest.getName().trim(),
          companyRequest.getDescription(),
          companyRequest.getWebsite(),
          companyRequest.getIndustry(),
          companyRequest.getCompanySize(),
          companyRequest.getLocation(),
          null,
           now,
           now
        );

        Company savedCompany =
                companyRepository.save(company);
        return mapToResponse(savedCompany);

    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> getMyCompanies(){
        Long userId = currentUserService.getCurrentUserId();
        User owner = userRepository.findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        List<Company> companyList =
                companyRepository.findAllByOwnerId(userId);
        List<CompanyResponse> companyResponses =
                new ArrayList<>();
        for(Company company:companyList){
            companyResponses.add(
                    mapToResponse(company)
            );
        }
        return companyResponses;
    }

    @Transactional(readOnly = true)
    public CompanyResponse getMyCompany(
            Long companyId
    ){
        Long userId = currentUserService.getCurrentUserId();
        User owner = userRepository.findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Company company =
                companyRepository.
                        findByIdAndOwnerId(companyId,userId).
                        orElseThrow(()->
                                new ResourceNotFoundException(
                                        "Company not found"
                                )
                        );
        return mapToResponse(company);
    }

    @Transactional
    public CompanyResponse updateCompany(
            CompanyRequest companyRequest,
            Long companyId
    ){
        Long userId = currentUserService.getCurrentUserId();
        User owner = userRepository.
                findById(userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "User not found"
                        )
        );

        Company company = companyRepository.
                findByIdAndOwnerId(companyId,userId).
                orElseThrow(()->
                new ResourceNotFoundException(
                        "Company not found"
                )
                );
        company.update(
                companyRequest.getName().trim(),
                companyRequest.getDescription(),
                companyRequest.getWebsite(),
                companyRequest.getIndustry(),
                companyRequest.getCompanySize(),
                companyRequest.getLocation()
        );


        return mapToResponse(company);

    }

    @Transactional
    public void deleteCompany(Long companyId){
        Long userId = currentUserService.getCurrentUserId();
        Company company =
                companyRepository.findByIdAndOwnerId(companyId,userId).
                orElseThrow(()->
                        new ResourceNotFoundException(
                                "Company not found"
                        )
                        );
        companyRepository.delete(company);
    }

    private CompanyResponse mapToResponse(
            Company savedCompany
    ){
        return new CompanyResponse(
                savedCompany.getId(),
                savedCompany.getName(),
                savedCompany.getDescription(),
                savedCompany.getWebsite(),
                savedCompany.getIndustry(),
                savedCompany.getCompanySize(),
                savedCompany.getLocation(),
                savedCompany.getLogoStorageKey() != null,
                savedCompany.getCreatedAt(),
                savedCompany.getUpdatedAt()
        );
    }


}
