package com.ga.meal.controller;

import com.ga.meal.entity.User;
import com.ga.meal.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;

    @PutMapping("/{id}/deactivate")
    public User deactivateUser(@PathVariable Long id) {
        return userService.deactivateUser(id);
    }
}
