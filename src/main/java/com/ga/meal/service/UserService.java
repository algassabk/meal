package com.ga.meal.service;

import com.ga.meal.dto.ChangePasswordRequest;
import com.ga.meal.dto.UpdateProfileRequest;
import com.ga.meal.entity.User;
import com.ga.meal.enums.UserStatus;
import com.ga.meal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Gets the currently logged-in user.
     */
    public User getCurrentUser() {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }

    public User getMyProfile() {
        return getCurrentUser();
    }

    public User updateMyProfile(UpdateProfileRequest request) {
        User currentUser = getCurrentUser();

        boolean emailChanged = !currentUser.getEmail().equalsIgnoreCase(request.getEmail());

        if (emailChanged && userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        currentUser.setFullName(request.getFullName());
        currentUser.setEmail(request.getEmail());

        return userRepository.save(currentUser);
    }

    public User uploadProfilePicture(MultipartFile file) {
        User currentUser = getCurrentUser();

        if (file.isEmpty()) {
            throw new RuntimeException("File is required");
        }

        String originalFileName = file.getOriginalFilename();
        String extension = "";

        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }

        String fileName = UUID.randomUUID() + extension;
        Path uploadPath = Paths.get("uploads");
        Path filePath = uploadPath.resolve(fileName);

        try {
            Files.createDirectories(uploadPath);
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Could not upload profile picture");
        }

        currentUser.setProfilePicture("/uploads/" + fileName);
        return userRepository.save(currentUser);
    }

    public void changePassword(ChangePasswordRequest request) {
        User currentUser = getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(currentUser);
    }

    public User deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUserStatus(UserStatus.INACTIVE);

        return userRepository.save(user);
    }
}
