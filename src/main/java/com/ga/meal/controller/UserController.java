package com.ga.meal.controller;

import com.ga.meal.dto.ChangePasswordRequest;
import com.ga.meal.dto.UpdateProfileRequest;
import com.ga.meal.entity.User;
import com.ga.meal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Returns current logged-in user.
     */
    @GetMapping("/me")
    public User getCurrentUser() {
        return userService.getCurrentUser();
    }

    @GetMapping("/profile")
    public User getMyProfile() {
        return userService.getMyProfile();
    }

    @PutMapping("/profile")
    public User updateMyProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {
        return userService.updateMyProfile(request);
    }

    @PostMapping("/profile-picture")
    public User uploadProfilePicture(
            @RequestParam("file") MultipartFile file
    ) {
        return userService.uploadProfilePicture(file);
    }

    @PutMapping("/change-password")
    public String changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(request);
        return "Password changed successfully";
    }
}
