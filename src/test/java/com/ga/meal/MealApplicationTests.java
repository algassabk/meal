package com.ga.meal;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ga.meal.entity.User;
import com.ga.meal.enums.Role;
import com.ga.meal.enums.UserStatus;
import com.ga.meal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MealApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {
    }

    @Test
    void registerAndLoginReturnJwtToken() throws Exception {
        String email = "mock-user-" + System.currentTimeMillis() + "@example.com";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "fullName", "Mock User",
                                "email", email,
                                "password", "password123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", "password123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void protectedUserRouteRequiresJwt() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isForbidden());
    }

    @Test
    void categoryValidationReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "",
                                "description", "Invalid category"
                        ))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.name").exists());
    }

    @Test
    void loggedInUserCanChangePassword() throws Exception {
        String email = "change-password-" + System.currentTimeMillis() + "@example.com";

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "fullName", "Password User",
                                "email", email,
                                "password", "password123"
                        ))))
                .andExpect(status().isOk());

        String token = loginAndGetToken(email, "password123");

        mockMvc.perform(put("/users/change-password")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "currentPassword", "password123",
                                "newPassword", "newpass123"
                        ))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", "newpass123"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void adminCanDeactivateUser() throws Exception {
        String adminEmail = "admin-test-" + System.currentTimeMillis() + "@example.com";
        String userEmail = "soft-delete-" + System.currentTimeMillis() + "@example.com";

        User admin = User.builder()
                .fullName("Admin Test")
                .email(adminEmail)
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .userStatus(UserStatus.ACTIVE)
                .isEmailVerified(true)
                .build();

        User user = User.builder()
                .fullName("Soft Delete User")
                .email(userEmail)
                .password(passwordEncoder.encode("password123"))
                .role(Role.USER)
                .userStatus(UserStatus.ACTIVE)
                .isEmailVerified(true)
                .build();

        userRepository.save(admin);
        User savedUser = userRepository.save(user);

        String adminToken = loginAndGetToken(adminEmail, "admin123");

        mockMvc.perform(put("/admin/users/" + savedUser.getId() + "/deactivate")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userStatus").value("INACTIVE"));

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", userEmail,
                                "password", "password123"
                        ))))
                .andExpect(status().isBadRequest());
    }

    private String loginAndGetToken(String email, String password) throws Exception {
        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "email", email,
                                "password", password
                        ))))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        return jsonNode.get("token").asText();
    }
}
