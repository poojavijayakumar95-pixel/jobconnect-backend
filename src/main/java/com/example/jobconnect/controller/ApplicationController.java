package com.example.jobconnect.controller;

import com.example.jobconnect.model.Application;
import com.example.jobconnect.services.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = "http://localhost:3000")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    // SEEKER: Apply to a job
    @PostMapping("/apply")
    public ResponseEntity<?> applyForJob(@RequestParam Long jobId, @RequestParam Long seekerId) {
        try {
            Application application = applicationService.applyForJob(jobId, seekerId);
            return new ResponseEntity<>(application, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catch the "already applied" exception we wrote in the service layer
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // EMPLOYER: View applications for a specific job
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Application>> getApplicationsForJob(@PathVariable Long jobId) {
        List<Application> applications = applicationService.getApplicationsForJob(jobId);
        return new ResponseEntity<>(applications, HttpStatus.OK);
    }

    // EMPLOYER: Update the status of an application
    @PutMapping("/{applicationId}/status")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long applicationId,
            @RequestParam String status) {
        
        Application updatedApplication = applicationService.updateStatus(applicationId, status);
        return new ResponseEntity<>(updatedApplication, HttpStatus.OK);
    }
}
