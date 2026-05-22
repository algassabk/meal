package com.ga.meal.controller;

import com.ga.meal.dto.AuthResponse;
import com.ga.meal.dto.EmailVerificationRequest;
import com.ga.meal.dto.LoginRequest;
import com.ga.meal.dto.PasswordResetConfirmRequest;
import com.ga.meal.dto.PasswordResetRequest;
import com.ga.meal.dto.RegisterRequest;
import com.ga.meal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Register endpoint.
     */
    @PostMapping("/register")
    public AuthResponse register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return authService.register(request);
    }

    /**
     * Login endpoint.
     */
    @PostMapping("/login")
    public AuthResponse login(
            @Valid @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @PostMapping("/email-verification/request")
    public String requestEmailVerification(
            @Valid @RequestBody EmailVerificationRequest request
    ) {
        return authService.createEmailVerificationToken(request);
    }

    @GetMapping("/email-verification/verify")
    public String verifyEmail(@RequestParam String token) {
        authService.verifyEmail(token);
        return "Email verified successfully";
    }

    @PostMapping("/password-reset/request")
    public String requestPasswordReset(
            @Valid @RequestBody PasswordResetRequest request
    ) {
        return authService.createPasswordResetToken(request);
    }

    @PostMapping("/password-reset/confirm")
    public String confirmPasswordReset(
            @Valid @RequestBody PasswordResetConfirmRequest request
    ) {
        authService.resetPassword(request);
        return "Password reset successfully";
    }
}
