package com.jobportal.repository;

import com.jobportal.entity.ApplicationStatusHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationStatusHistoryRepository
        extends JpaRepository<ApplicationStatusHistory, Long> {

    Page<ApplicationStatusHistory> findByApplicationId(
            Long applicationId,
            Pageable pageable
    );
}