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

@ExtendWith(MockitoExtension.class) 
public class ApplicationServiceTest {

    
    @Mock
    private ApplicationRepository applicationRepository;
    
    @Mock
    private JobRepository jobRepository;
    
    @Mock
    private UserRepository userRepository;

    
    @InjectMocks
    private ApplicationService applicationService;

    @Test
    public void testApplyForJob_AlreadyApplied_ThrowsException() {
        
        Long jobId = 1L;
        Long seekerId = 2L;
        
        
        when(applicationRepository.existsByJobIdAndApplicantId(jobId, seekerId)).thenReturn(true);

        
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            applicationService.applyForJob(jobId, seekerId);
        });

        
        assertEquals("You have already applied for this job.", exception.getMessage());
        
        
        verify(applicationRepository, never()).save(any(Application.class));
    }
}