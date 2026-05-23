package com.ga.meal.service;

import com.ga.meal.dto.AuthResponse;
import com.ga.meal.dto.EmailVerificationRequest;
import com.ga.meal.dto.LoginRequest;
import com.ga.meal.dto.PasswordResetConfirmRequest;
import com.ga.meal.dto.PasswordResetRequest;
import com.ga.meal.dto.RegisterRequest;
import com.ga.meal.entity.EmailVerificationToken;
import com.ga.meal.entity.PasswordResetToken;
import com.ga.meal.entity.User;
import com.ga.meal.enums.Role;
import com.ga.meal.enums.UserStatus;
import com.ga.meal.repository.EmailVerificationTokenRepository;
import com.ga.meal.repository.PasswordResetTokenRepository;
import com.ga.meal.repository.UserRepository;
import com.ga.meal.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Registers a new user.
     */
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .userStatus(UserStatus.ACTIVE)
                .isEmailVerified(false)
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserStatus() == UserStatus.INACTIVE) {
            throw new RuntimeException("User account is inactive");
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    public String createEmailVerificationToken(EmailVerificationRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (Boolean.TRUE.equals(user.getIsEmailVerified())) {
            throw new RuntimeException("Email is already verified");
        }

        String token = UUID.randomUUID().toString();

        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .user(user)
                .build();

        emailVerificationTokenRepository.save(verificationToken);

        // TODO: Send this token by email when email service is added.
        return "Email verification token: " + token;
    }

    public void verifyEmail(String token) {
        EmailVerificationToken verificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid verification token"));

        if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Verification token expired");
        }

        User user = verificationToken.getUser();
        user.setIsEmailVerified(true);

        userRepository.save(user);
        emailVerificationTokenRepository.delete(verificationToken);
    }

    public String createPasswordResetToken(PasswordResetRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .expiryDate(LocalDateTime.now().plusHours(1))
                .user(user)
                .build();

        passwordResetTokenRepository.save(passwordResetToken);

        // TODO: Send this token by email when email service is added.
        return "Password reset token: " + token;
    }

    public void resetPassword(PasswordResetConfirmRequest request) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid password reset token"));

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Password reset token expired");
        }

        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));

        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetToken);
    }
}
