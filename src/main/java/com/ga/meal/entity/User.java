package com.ga.meal.entity;

import com.ga.meal.enums.Role;
import com.ga.meal.enums.UserStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Column(name = "is_email_verified")
    private Boolean isEmailVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_status")
    private UserStatus userStatus = UserStatus.ACTIVE;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Sets the creation and update time before saving a new user.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (role == null) {
            role = Role.USER;
        }

        if (isEmailVerified == null) {
            isEmailVerified = false;
        }

        if (userStatus == null) {
            userStatus = UserStatus.ACTIVE;
        }
    }

    /**
     * Updates the updatedAt time before updating user data.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}