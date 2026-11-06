package com.jobPortal.careerBridge.controller;

import com.jobPortal.careerBridge.dto.request.CompanyRequest;
import com.jobPortal.careerBridge.dto.response.CompanyResponse;
import com.jobPortal.careerBridge.service.CompanyService;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(
            CompanyService companyService
    ){
        this.companyService = companyService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('COMPANY_CREATE')")
    public ResponseEntity<CompanyResponse> createCompany(
            @Valid @RequestBody CompanyRequest companyRequest
            ){
        CompanyResponse companyResponse =
                companyService.createCompany(companyRequest);
        return ResponseEntity.
                status(HttpStatus.CREATED).
                body(companyResponse);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    public ResponseEntity<List<CompanyResponse>> getMyCompanies(){
        List<CompanyResponse> companiesList =
                companyService.getMyCompanies();
        return ResponseEntity.
                status(HttpStatus.OK).
                body(companiesList);
    }

    @GetMapping("/{companyId}")
    @PreAuthorize("hasAuthority('COMPANY_VIEW')")
    public ResponseEntity<CompanyResponse> getMyCompanyById(@PathVariable Long companyId){
        CompanyResponse companyResponse =
                companyService.getMyCompany(companyId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(companyResponse);
    }

    @PutMapping("/{companyId}")
    @PreAuthorize("hasAuthority('COMPANY_UPDATE')")
    public ResponseEntity<CompanyResponse> updateMyCompanyById(
            @Valid @RequestBody CompanyRequest companyRequest,
            @PathVariable Long companyId
    ){
        CompanyResponse companyResponse =
                companyService.updateCompany(companyRequest,companyId);
        return ResponseEntity.
                status(HttpStatus.OK).
                body(companyResponse);
    }

    @DeleteMapping("/{companyId}")
    @PreAuthorize("hasAuthority('COMPANY_DELETE')")
    public ResponseEntity<Void> deleteCompanyById(
            @PathVariable Long companyId
    ){
        companyService.deleteCompany(companyId);
        return ResponseEntity.
                status(HttpStatus.NO_CONTENT).
                build();
    }



}
