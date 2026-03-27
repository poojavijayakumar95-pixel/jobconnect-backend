package com.example.jobconnect;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        
        "TWILIO_SID=dummy_test_sid_123",
        "TWILIO_TOKEN=dummy_test_token_123", 
        "twilio.phone.number=+1234567890"
})
class JobconnectApplicationTests {

    @Test
    void contextLoads() {
        
    }

}