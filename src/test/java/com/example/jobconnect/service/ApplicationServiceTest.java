package com.example.jobconnect.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.jobconnect.model.Application;
import com.example.jobconnect.repository.ApplicationRepository;
import com.example.jobconnect.repository.JobRepository;
import com.example.jobconnect.repository.UserRepository;
import com.example.jobconnect.services.ApplicationService;

@ExtendWith(MockitoExtension.class) // Tells JUnit to enable Mockito
public class ApplicationServiceTest {

    
    @Mock
    private ApplicationRepository applicationRepository;
    
    @Mock
    private JobRepository jobRepository;
    
    @Mock
    private UserRepository userRepository;

    // @InjectMocks creates a real ApplicationService but injects the fake repositories into it
    @InjectMocks
    private ApplicationService applicationService;

    @Test
    public void testApplyForJob_AlreadyApplied_ThrowsException() {
        // 1. ARRANGE: Set up our fake data and rules
        Long jobId = 1L;
        Long seekerId = 2L;
        
        // We tell our fake repository: "If anyone asks if this user applied to this job, say TRUE"
        when(applicationRepository.existsByJobIdAndApplicantId(jobId, seekerId)).thenReturn(true);

        // 2. ACT & ASSERT: Try to apply, and prove that it throws our custom RuntimeException
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applicationService.applyForJob(jobId, seekerId);
        });

        // Prove the error message is exactly what we expect
        assertEquals("You have already applied for this job.", exception.getMessage());
        
        // Prove that we NEVER tried to save a new application to the database
        verify(applicationRepository, never()).save(any(Application.class));
    }
}