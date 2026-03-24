package com.example.jobconnect.security;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter) {
        this.jwtRequestFilter = jwtRequestFilter;
    }

    // 1. Password Encoder Bean (Hashes our passwords)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Authentication Manager Bean (Handles the login process)
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // 3. Security Filter Chain (The main configuration)
 // Add this right before the final closing brace of the SecurityConfig class
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // 1. Allow your React/Vite port
        configuration.setAllowedOrigins(List.of("http://localhost:5173","https://jobconnect-project1.netlify.app")); 
        
        // 2. Allow all standard HTTP methods, including OPTIONS (the preflight request)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        // 3. Allow headers like Authorization (for our JWT) and Content-Type (for JSON)
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 4. Apply these rules to every single API endpoint (/**)
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            // Disable CSRF because we are using JWTs, not session cookies
            .csrf(csrf -> csrf.disable())
            
            // Set session management to stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Define route permissions
            .authorizeHttpRequests(auth -> auth
                // Public endpoints (anyone can register or login)
            		// Public endpoints
                    .requestMatchers("/api/auth/**").permitAll()
                    
                    // Allow Swagger UI
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                
                .requestMatchers("/error").permitAll() 
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                // Job endpoints
                .requestMatchers(HttpMethod.POST, "/api/jobs/create").hasRole("EMPLOYER") // Only employers can post
                .requestMatchers(HttpMethod.GET, "/api/jobs/search").hasAnyRole("JOB_SEEKER", "EMPLOYER") // Anyone logged in can search
                .requestMatchers("/api/jobs/employer/**").hasRole("EMPLOYER")
                
                // Application endpoints
                .requestMatchers(HttpMethod.POST, "/api/applications/apply").hasRole("JOB_SEEKER") // Only seekers can apply
                .requestMatchers("/api/applications/job/**").hasRole("EMPLOYER") // Only employers can view applications for a job
                .requestMatchers(HttpMethod.PUT, "/api/applications/*/status").hasRole("EMPLOYER")
                
                // Any other request must be authenticated
                .anyRequest().authenticated()
            );

        // Add our custom JWT filter BEFORE the standard Spring Security password filter
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
