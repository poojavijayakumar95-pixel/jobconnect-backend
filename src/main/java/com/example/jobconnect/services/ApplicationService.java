package com.example.jobconnect.services;

import com.example.jobconnect.model.Application;
import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.ApplicationRepository;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {
	

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final SmsService smsService;

    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository, UserRepository userRepository,SmsService smsService) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.smsService = smsService;
    }

    
    public Application applyForJob(Long jobId, Long seekerId) {
        
        if (applicationRepository.existsByJobIdAndApplicantId(jobId, seekerId)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        User seeker = userRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        
        Application application = new Application(job, seeker);
        return applicationRepository.save(application);
    }

    
    
    public Application updateStatus(Long applicationId, String newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(newStatus);
        Application savedApplication = applicationRepository.save(application);

        
        String applicantPhone = application.getApplicant().getPhoneNumber(); 
        String jobTitle = application.getJob().getTitle();
        String message = "Hello! Your application status for " + jobTitle + " has been updated to: " + newStatus;
        
        if (applicantPhone != null && !applicantPhone.isEmpty()) {
            smsService.sendSms(applicantPhone, message);
        }

        return savedApplication;
    }

    
    public List<Application> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}

