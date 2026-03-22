package com.example.jobconnect.dto;

public class AuthResponse {
    private String jwt;
    private Long userId; // Add this!

    public AuthResponse(String jwt, Long userId) {
        this.jwt = jwt;
        this.userId = userId;
    }

    public String getJwt() { return jwt; }
    public Long getUserId() { return userId; }
}
