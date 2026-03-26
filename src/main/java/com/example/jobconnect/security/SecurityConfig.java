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

    
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        
        configuration.setAllowedOrigins(List.of("http://localhost:5173","https://jobconnect-project1.netlify.app")); 
        
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        
        
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
           
            .csrf(csrf -> csrf.disable())
            
            
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
           
            .authorizeHttpRequests(auth -> auth
               
                    .requestMatchers("/api/auth/**").permitAll()
                    
                    
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api/auth/**").permitAll()
                
                .requestMatchers("/error").permitAll() 
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                
                
                .requestMatchers(HttpMethod.POST, "/api/jobs/create").hasRole("EMPLOYER") 
                .requestMatchers(HttpMethod.GET, "/api/jobs/search").hasAnyRole("JOB_SEEKER", "EMPLOYER") 
                .requestMatchers("/api/jobs/employer/**").hasRole("EMPLOYER")
                
                
                .requestMatchers(HttpMethod.POST, "/api/applications/apply").hasRole("JOB_SEEKER") 
                .requestMatchers("/api/applications/job/**").hasRole("EMPLOYER") 
                .requestMatchers(HttpMethod.PUT, "/api/applications/*/status").hasRole("EMPLOYER")
                
                
                .anyRequest().authenticated()
            );

        
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
