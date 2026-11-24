package com.jobPortal.careerBridge.specification;

import com.jobPortal.careerBridge.entity.Job;
import com.jobPortal.careerBridge.enums.EmploymentType;
import com.jobPortal.careerBridge.enums.JobStatus;
import com.jobPortal.careerBridge.enums.WorkplaceType;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;


public class JobSpecification {

    public static Specification<Job> hasPublishedStatus(){
        return (root, query,criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        JobStatus.PUBLISHED
                );
    }

    public static Specification<Job> hasKeyword(String keyword){
       return (root, query, criteriaBuilder) -> {
         String searchKeyword = "%"+keyword.toLowerCase()+"%";
         return criteriaBuilder.or(
           criteriaBuilder.like(
                   criteriaBuilder.lower(root.get("title")),
                   searchKeyword
           ),
           criteriaBuilder.like(
                   criteriaBuilder.lower(root.get("description")),
                   searchKeyword
           )
         );
       };
    }

    public static Specification<Job> hasLocation(String location){
        return (root, query, criteriaBuilder) ->{
            String searchLocation = "%"+location.toLowerCase()+"%";
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("location")),
                    searchLocation
            );
        };
    }

    public static Specification<Job> hasEmploymentType(EmploymentType employmentType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("employmentType"),
                        employmentType
                );
    }

    public static Specification<Job> hasWorkplaceType(WorkplaceType workplaceType){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("workplaceType"),
                        workplaceType
                );
    }

    public static Specification<Job> hasExperience(Integer experience){
        return (root, query, criteriaBuilder) -> {
          return criteriaBuilder.and(
           criteriaBuilder.lessThanOrEqualTo(
                   root.get("experienceMin"),
                   experience
           ),
           criteriaBuilder.greaterThanOrEqualTo(
                   root.get("experienceMax"),
                   experience
           )
          );
        };
    }

    public static Specification<Job> hasMinimumSalary(BigDecimal minimumSalary){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("salaryMax"),
                        minimumSalary
                );
    }

    public static Specification<Job> hasMaximumSalary(BigDecimal maximumSalary){
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(
                        root.get("salaryMin"),
                        maximumSalary
                );
    }


}
