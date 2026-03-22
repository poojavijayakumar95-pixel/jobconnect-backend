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

    // SEEKER: Apply for a job
    public Application applyForJob(Long jobId, Long seekerId) {
        // 1. Check if application already exists to prevent duplicates
        if (applicationRepository.existsByJobIdAndApplicantId(jobId, seekerId)) {
            throw new RuntimeException("You have already applied for this job.");
        }

        // 2. Fetch the Job and the Seeker from the database
        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        
        User seeker = userRepository.findById(seekerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Create and save the new application
        Application application = new Application(job, seeker);
        return applicationRepository.save(application);
    }

    // EMPLOYER: Update application status (e.g., REJECTED, HIRED)
    /*public Application updateStatus(Long applicationId, String newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(newStatus);
        return applicationRepository.save(application);
        
    }*/
    
    public Application updateStatus(Long applicationId, String newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        
        application.setStatus(newStatus);
        Application savedApplication = applicationRepository.save(application);

        // --- Trigger SMS Notification ---
        String applicantPhone = application.getApplicant().getPhoneNumber(); // Assuming you added this to User.java
        String jobTitle = application.getJob().getTitle();
        String message = "Hello! Your application status for " + jobTitle + " has been updated to: " + newStatus;
        
        if (applicantPhone != null && !applicantPhone.isEmpty()) {
            smsService.sendSms(applicantPhone, message);
        }

        return savedApplication;
    }

    // EMPLOYER: View all applications for a specific job
    public List<Application> getApplicationsForJob(Long jobId) {
        return applicationRepository.findByJobId(jobId);
    }
}

