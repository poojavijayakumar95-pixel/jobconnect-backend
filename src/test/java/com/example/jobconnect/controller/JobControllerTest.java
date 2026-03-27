package com.example.jobconnect.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.security.CustomUserDetailsService;
import com.example.jobconnect.security.JwtRequestFilter;
import com.example.jobconnect.security.JwtUtil;
import com.example.jobconnect.security.SecurityConfig;
import com.example.jobconnect.services.JobService;
import com.fasterxml.jackson.databind.ObjectMapper;


@WebMvcTest(JobController.class)
@Import({SecurityConfig.class, JwtRequestFilter.class})
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper; 

    @MockBean
    private JobService jobService;

    
    @MockBean
    private JwtUtil jwtUtil;
    @MockBean
    private CustomUserDetailsService customUserDetailsService;
   // @MockBean
    //private JwtRequestFilter jwtRequestFilter; 

    @Test
    @WithMockUser(roles = "EMPLOYER") 
    public void testCreateJob_AsEmployer_ReturnsCreated() throws Exception {
        
        
        Job newJob = new Job("Backend Developer", "Java Spring Boot", "Remote", 100000.0, LocalDate.now(), null);
        newJob.setId(1L);

        Mockito.when(jobService.createJob(any(Job.class), eq(1L))).thenReturn(newJob);

        
        mockMvc.perform(post("/api/jobs/create")
                .param("employerId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJob))
                .with(csrf())) 
                .andExpect(status().isCreated()) 
                .andExpect(jsonPath("$.title").value("Backend Developer")) 
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    @WithMockUser(roles = "JOB_SEEKER") 
    public void testCreateJob_AsJobSeeker_ReturnsForbidden() throws Exception {
        
        Job newJob = new Job("Backend Developer", "Java Spring Boot", "Remote", 100000.0, LocalDate.now(), null);

        
        mockMvc.perform(post("/api/jobs/create")
                .param("employerId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newJob))
                .with(csrf()))
                
                .andExpect(status().isForbidden()); 
    }
}
