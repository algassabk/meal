package com.ga.meal.config;

import com.ga.meal.entity.Category;
import com.ga.meal.entity.Ingredient;
import com.ga.meal.entity.User;
import com.ga.meal.enums.Role;
import com.ga.meal.enums.UserStatus;
import com.ga.meal.repository.CategoryRepository;
import com.ga.meal.repository.IngredientRepository;
import com.ga.meal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        seedCategories();
        seedIngredients();
        seedAdminUser();
    }

    private void seedCategories() {
        if (categoryRepository.count() == 0) {
            categoryRepository.save(Category.builder()
                    .name("Breakfast")
                    .description("Morning meal recipes")
                    .build());

            categoryRepository.save(Category.builder()
                    .name("Lunch")
                    .description("Lunch meal recipes")
                    .build());

            categoryRepository.save(Category.builder()
                    .name("Dinner")
                    .description("Dinner meal recipes")
                    .build());
        }
    }

    private void seedIngredients() {
        if (ingredientRepository.count() == 0) {
            ingredientRepository.save(Ingredient.builder()
                    .name("Rice")
                    .unit("cup")
                    .build());

            ingredientRepository.save(Ingredient.builder()
                    .name("Chicken")
                    .unit("gram")
                    .build());

            ingredientRepository.save(Ingredient.builder()
                    .name("Tomato")
                    .unit("piece")
                    .build());
        }
    }

    private void seedAdminUser() {
        if (!userRepository.existsByEmail("admin@meal.com")) {
            User admin = User.builder()
                    .fullName("Meal Admin")
                    .email("admin@meal.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(Role.ADMIN)
                    .userStatus(UserStatus.ACTIVE)
                    .isEmailVerified(true)
                    .build();

            userRepository.save(admin);
        }
    }
}
