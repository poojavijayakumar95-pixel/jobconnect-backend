package com.example.jobconnect.repository;

import com.example.jobconnect.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    // For the Employer: Find all jobs posted by a specific user ID
    List<Job> findByEmployerId(Long employerId);

    // For the Job Seeker: Search jobs by title OR location (case-insensitive)
    List<Job> findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(String title, String location);
}
