package com.example.jobconnect.services;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;
    private final UserRepository userRepository;

   
    public JobService(JobRepository jobRepository, UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    
    public Job createJob(Job job, Long employerId) {
        User employer = userRepository.findById(employerId)
                .orElseThrow(() -> new RuntimeException("Employer not found"));
        
        job.setEmployer(employer);
        return jobRepository.save(job);
    }

    
    public List<Job> searchJobs(String keyword, String location) {
        if (keyword != null || location != null) {
            return jobRepository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase(keyword, location);
        }
        return jobRepository.findAll();
    }

    
    public List<Job> getJobsByEmployer(Long employerId) {
        return jobRepository.findByEmployerId(employerId);
    }
}
