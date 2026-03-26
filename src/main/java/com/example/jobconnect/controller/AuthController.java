package com.example.jobconnect.controller;

import com.example.jobconnect.dto.AuthRequest;
import com.example.jobconnect.dto.AuthResponse;
import com.example.jobconnect.model.User;
import com.example.jobconnect.repository.UserRepository;
import com.example.jobconnect.security.CustomUserDetailsService;
import com.example.jobconnect.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://jobconnect-project1.netlify.app/")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, 
                          CustomUserDetailsService userDetailsService, 
                          JwtUtil jwtUtil, 
                          UserRepository userRepository, 
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        
        User newUser = new User(
                authRequest.getEmail(),
                passwordEncoder.encode(authRequest.getPassword()),
                authRequest.getRole(),
                authRequest.getPhoneNumber() 
        );

        userRepository.save(newUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

   
    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);

        
        User user = userRepository.findByEmail(authRequest.getEmail()).get();

        
        return ResponseEntity.ok(new AuthResponse(jwt, user.getId()));
    }
}
