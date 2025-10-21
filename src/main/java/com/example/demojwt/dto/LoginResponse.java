package com.example.demojwt.dto;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private String message;
    public LoginResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}
