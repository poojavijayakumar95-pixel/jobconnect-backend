package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "http://localhost:3000") // Allows your React app to talk to this API
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // EMPLOYER: Create a new job listing
    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job, @RequestParam Long employerId) {
        /* Note: Right now we pass employerId as a parameter. 
           Once we implement JWT, we will extract the ID securely from the logged-in user's token instead! */
        Job createdJob = jobService.createJob(job, employerId);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    // SEEKER: Search for jobs
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        
        List<Job> jobs = jobService.searchJobs(keyword, location);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    // EMPLOYER: Get all jobs posted by them
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<Job>> getJobsByEmployer(@PathVariable Long employerId) {
        List<Job> jobs = jobService.getJobsByEmployer(employerId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
