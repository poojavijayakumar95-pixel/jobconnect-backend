package com.example.jobconnect.repository;

import com.example.jobconnect.model.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    
    List<Application> findByApplicantId(Long applicantId);

    
    List<Application> findByJobId(Long jobId);

    
    boolean existsByJobIdAndApplicantId(Long jobId, Long applicantId);
}
