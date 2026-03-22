package com.example.jobconnect.repository;

import com.example.jobconnect.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    // For the Job Seeker: View all applications they have submitted
    List<Application> findByApplicantId(Long applicantId);

    // For the Employer: View all applications for a specific job they posted
    List<Application> findByJobId(Long jobId);

    // To prevent a user from applying to the exact same job twice
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
}
