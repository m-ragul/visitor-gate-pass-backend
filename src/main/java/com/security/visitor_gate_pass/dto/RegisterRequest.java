package com.security.visitor_gate_pass.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String role; // Optional role request
    private String adminSecret; // Required if requesting ADMIN/GUARD
}