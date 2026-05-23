package com.ga.meal.controller;

import com.ga.meal.dto.ChangePasswordRequest;
import com.ga.meal.entity.User;
import com.ga.meal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/change-password")
    public String changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(request);
        return "Password changed successfully";
    }
}
