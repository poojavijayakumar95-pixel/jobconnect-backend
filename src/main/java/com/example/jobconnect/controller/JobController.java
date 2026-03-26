package com.example.jobconnect.controller;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.services.JobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
@CrossOrigin(origins = "https://jobconnect-project1.netlify.app/")  
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    
    @PostMapping("/create")
    public ResponseEntity<Job> createJob(@RequestBody Job job, @RequestParam Long employerId) {
        
        Job createdJob = jobService.createJob(job, employerId);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    
    @GetMapping("/search")
    public ResponseEntity<List<Job>> searchJobs(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String location) {
        
        List<Job> jobs = jobService.searchJobs(keyword, location);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    
    @GetMapping("/employer/{employerId}")
    public ResponseEntity<List<Job>> getJobsByEmployer(@PathVariable Long employerId) {
        List<Job> jobs = jobService.getJobsByEmployer(employerId);
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }
}
