package com.jobPortal.careerBridge.service;

import com.jobPortal.careerBridge.dto.request.JobRequest;
import com.jobPortal.careerBridge.dto.request.JobSearchRequest;
import com.jobPortal.careerBridge.dto.response.JobResponse;
import com.jobPortal.careerBridge.dto.response.PageResponse;
import com.jobPortal.careerBridge.entity.Job;
import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import com.jobPortal.careerBridge.exception.BusinessRuleException;
import com.jobPortal.careerBridge.exception.InvalidSearchParameterException;
import com.jobPortal.careerBridge.repository.JobRepository;
import com.jobPortal.careerBridge.specification.JobSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class JobSearchService {

    // use this construction injection method or
    // @RequiredArgsConstructor  above JobSearchService class as annotation
    private final JobRepository jobRepository;

    @Autowired
    public JobSearchService(
            JobRepository jobRepository
    ){
        this.jobRepository = jobRepository;
    }

    public void validateJobSearchRequest(
            JobSearchRequest searchRequest
    ){
        if(searchRequest == null){
            throw new BusinessRuleException(
              "Search request cannot be null"
            );
        }
        String searchKeyword = searchRequest.getKeyword();
        String searchLocation = searchRequest.getLocation();
        EmploymentType employmentType = searchRequest.getEmploymentType();
        WorkplaceType workplaceType = searchRequest.getWorkplaceType();
        Integer experience = searchRequest.getExperience();
        BigDecimal minimumSalary = searchRequest.getMinimumSalary();
        BigDecimal maximumSalary = searchRequest.getMaximumSalary();
        boolean noKeyword = searchKeyword == null ||
                searchKeyword.isBlank();
        boolean noLocation = searchLocation == null ||
                searchLocation.isBlank();
        boolean noEmploymentType = employmentType == null ;
        boolean noWorkplaceType = workplaceType == null;
        boolean noExperience = experience == null;
        boolean noMinimumSalary = minimumSalary == null;
        boolean noMaximumSalary = maximumSalary == null;


        if(noKeyword && noLocation && noEmploymentType
        && noWorkplaceType && noExperience && noMinimumSalary
        && noMaximumSalary){
            throw new InvalidSearchParameterException(
                    "At least one search filter must be provided"
            );
        }

        if(!noMinimumSalary && !noMaximumSalary &&
                minimumSalary.compareTo(maximumSalary) > 0){
            throw new InvalidSearchParameterException(
              "Minimum salary cannot exceed maximum salary"
            );
        }


    }

    private Specification<Job> buildSpecification(JobSearchRequest request){

        //Every public search must return published jobs only
        Specification<Job> jobSpecification =
                JobSpecification.hasPublishedStatus();

        // Keyword filter
        if(request.getKeyword() != null
        && !request.getKeyword().isBlank()){
            jobSpecification = jobSpecification.
                    and(
                    JobSpecification.hasKeyword(
                            request.getKeyword().trim()
                    )
            );
        }

        // Location filter
        if(request.getLocation() != null &&
        !request.getLocation().isBlank()){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasLocation(
                              request.getLocation().trim()
                      )
                    );
        }

        // EmploymentType filter
        if(request.getEmploymentType() != null){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasEmploymentType(
                              request.getEmploymentType()
                      )
                    );
        }

        // WorkplaceType filter
        if(request.getWorkplaceType() != null){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasWorkplaceType(
                              request.getWorkplaceType()
                      )
                    );
        }

        // Experience filter
        if(request.getExperience()!= null){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasExperience(
                              request.getExperience()
                      )
                    );
        }

        // Minimum salary filter
        if(request.getMinimumSalary() != null){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasMinimumSalary(
                              request.getMinimumSalary()
                      )
                    );
        }

        // Maximum salary filter
        if(request.getMaximumSalary() != null){
            jobSpecification = jobSpecification.
                    and(
                      JobSpecification.hasMaximumSalary(
                              request.getMaximumSalary()
                      )
                    );
        }

        return jobSpecification;
    }

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
         "createdAt",
         "title",
         "salaryMin"
    );

    public PageResponse<JobResponse> searchJobs(
            JobSearchRequest searchRequest,
            int page,
            int size,
            String sortBy,
            String direction
    ){

        // 1.validate search request
        validateJobSearchRequest(searchRequest);

        // 2. validate pagination
        if(page < 0){
            throw  new InvalidSearchParameterException(
                    "Page number cannot be negative"
            );
        }
        if(size < 1 || size > 50){
            throw new InvalidSearchParameterException(
                    "Page size must be between 1 and 50"
            );
        }

        if(sortBy == null || sortBy.isBlank()){
            sortBy = "createdAt";
        }

        if(direction == null){
            direction = "DESC";
        }

        sortBy = sortBy.trim();

        if(!ALLOWED_SORT_FIELDS.contains(sortBy)){
            throw new InvalidSearchParameterException(
              "Invalid sort field : "+sortBy
            );
        }

        direction = direction.toLowerCase().trim();
        if(!direction.equals("desc") &&
        !direction.equals("asc")){
            throw new InvalidSearchParameterException(
                    "Invalid sort direction. Allowed values: ASC, DESC"
            );
        }

        // changing direction (String type) to Sort.Direction(Enum)
        // cause Sort.by() doesn't accepts "asc" or "desc" as it's direction argument.
        Sort.Direction sortDirection =
                Sort.Direction.fromString(direction);

        // sorting instructions
        Sort sort = Sort.by(sortDirection,sortBy).
                and(Sort.by(Sort.Direction.DESC,"id"));

        // pagination creation
        Pageable pageable = PageRequest.of(page,size,sort);

        // build specification filters
        Specification<Job> jobSpecification =
                buildSpecification(searchRequest);

        Page<Job> jobPage = jobRepository.findAll(
                jobSpecification,
                pageable
        );

        Page<JobResponse> responsePage = jobPage.map(
                job -> mapToResponse(job)
        );

        return PageResponse.from(responsePage);
    }

//    public List<JobResponse> searchJobs(JobSearchRequest searchRequest){
//
//        // 1. validating search request
//        validateJobSearchRequest(searchRequest);
//
//        // 2. Build dynamic specifications
//        Specification<Job> jobSpecification =
//                buildSpecification(searchRequest);
//
//        // 3. Execute database query
//        List<Job> jobs =
//                jobRepository.findAll(jobSpecification);
//
//       // 4. Converting entities to DTO's
//        List<JobResponse> jobResponseList = new ArrayList<>();
//        for(Job job : jobs){
//            jobResponseList.add(
//                    mapToResponse(job)
//            );
//        }
//        return jobResponseList;
//    }

    private JobResponse mapToResponse(Job savedJob){
        return new JobResponse(
                savedJob.getId(),
                savedJob.getCompany().getId(),
                savedJob.getCompany().getName(),
                savedJob.getTitle(),
                savedJob.getDescription(),
                savedJob.getEmploymentType(),
                savedJob.getWorkplaceType(),
                savedJob.getLocation(),
                savedJob.getExperienceMin(),
                savedJob.getExperienceMax(),
                savedJob.getSalaryMin(),
                savedJob.getSalaryMax(),
                savedJob.getVacancies(),
                savedJob.getStatus(),
                savedJob.getApplicationDeadline(),
                savedJob.getPublishedAt(),
                savedJob.getCreatedAt(),
                savedJob.getUpdatedAt()
        );
    }




}
