package com.jobportal.specification;

import com.jobportal.entity.EmploymentType;
import com.jobportal.entity.Job;
import com.jobportal.entity.JobStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class JobSpecification {

    public static Specification<Job> hasTitle(String title) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")),
                        "%" + title.toLowerCase() + "%"
                );
    }

    public static Specification<Job> hasStatus(JobStatus status) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("status"),
                        status
                );
    }

    public static Specification<Job> hasLocation(String location) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("location")),
                        "%" + location.toLowerCase() + "%"
                );
    }


    public static Specification<Job> hasEmploymentType(
            EmploymentType employmentType) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("employmentType"),
                        employmentType
                );
    }

    public static Specification<Job> hasMinimumSalary(
            Double minSalary) {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(
                        root.get("salary"),
                        minSalary
                );
    }

    public static Specification<Job> hasFutureDeadline() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThan(
                        root.get("applicationDeadline"),
                        LocalDateTime.now()
                );
    }
}