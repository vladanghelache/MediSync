package com.medisync.MediSync.controller;

import com.medisync.MediSync.dto.AuthTokenDto;
import com.medisync.MediSync.dto.CredentialsDto;
import com.medisync.MediSync.service.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @SecurityRequirements()
    public ResponseEntity<AuthTokenDto> login(@Valid @RequestBody CredentialsDto credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }
}
