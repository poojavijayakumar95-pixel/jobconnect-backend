package com.example.jobconnect.repository;

import com.example.jobconnect.model.Job;
import com.example.jobconnect.model.Role;
import com.example.jobconnect.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // Configures an in-memory database just for these tests
public class JobRepositoryTest {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSearchJobsByKeyword() {
        // 1. ARRANGE: Create and save a user and a job into the temporary H2 database
        User employer = new User("emp@test.com", "pass", Role.EMPLOYER, "123");
        userRepository.save(employer);

        Job job1 = new Job("Java Developer", "Spring Boot expert needed", "New York", 90000.0, LocalDate.now(), employer);
        Job job2 = new Job("Frontend Dev", "React expert needed", "Remote", 80000.0, LocalDate.now(), employer);
        jobRepository.save(job1);
        jobRepository.save(job2);

        // 2. ACT: Call our custom repository search method looking for "java"
        List<Job> foundJobs = jobRepository.findByTitleContainingIgnoreCaseOrLocationContainingIgnoreCase("java", "java");

        // 3. ASSERT: We should only find 1 job, and its title should be "Java Developer"
        assertEquals(1, foundJobs.size());
        assertEquals("Java Developer", foundJobs.get(0).getTitle());
    }
}
