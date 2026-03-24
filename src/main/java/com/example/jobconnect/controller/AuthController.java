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

    // 1. REGISTER ENDPOINT
   /* @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        
        // Check if email is already taken
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Create new user and hash the password
        User newUser = new User(
                authRequest.getEmail(),
                passwordEncoder.encode(authRequest.getPassword()), 
                authRequest.getRole()
        );

        userRepository.save(newUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }*/
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AuthRequest authRequest) {
        
        if (userRepository.existsByEmail(authRequest.getEmail())) {
            return new ResponseEntity<>("Email is already in use!", HttpStatus.BAD_REQUEST);
        }

        // Update this block to include the phone number
        User newUser = new User(
                authRequest.getEmail(),
                passwordEncoder.encode(authRequest.getPassword()),
                authRequest.getRole(),
                authRequest.getPhoneNumber() 
        );

        userRepository.save(newUser);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    // 2. LOGIN ENDPOINT
   /* @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // Tell Spring Security to verify the email and password
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            // If the password doesn't match the hash in the database, this error is thrown
            return new ResponseEntity<>("Incorrect email or password", HttpStatus.UNAUTHORIZED);
        }

        // If authentication is successful, fetch the user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getEmail());

        // Generate the JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // Send the token back to the React frontend
        return ResponseEntity.ok(new AuthResponse(jwt));
    }
}*/
 // 2. LOGIN ENDPOINT
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

        // Fetch the user from the DB to get their ID
        User user = userRepository.findByEmail(authRequest.getEmail()).get();

        // Send BOTH the token and the ID to React
        return ResponseEntity.ok(new AuthResponse(jwt, user.getId()));
    }
}
