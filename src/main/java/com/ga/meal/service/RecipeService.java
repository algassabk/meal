package com.ga.meal.service;

import com.ga.meal.dto.RecipeRequest;
import com.ga.meal.entity.Category;
import com.ga.meal.entity.Recipe;
import com.ga.meal.entity.User;
import com.ga.meal.enums.RecipeStatus;
import com.ga.meal.repository.CategoryRepository;
import com.ga.meal.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;

    public Recipe createRecipe(RecipeRequest request) {
        User currentUser = userService.getCurrentUser();

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Recipe recipe = Recipe.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .prepTime(request.getPrepTime())
                .cookTime(request.getCookTime())
                .servings(request.getServings())
                .imageUrl(request.getImageUrl())
                .status(RecipeStatus.PENDING)
                .user(currentUser)
                .category(category)
                .build();

        return recipeRepository.save(recipe);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));
    }

    public Recipe updateRecipe(Long id, RecipeRequest request) {
        Recipe recipe = getRecipeById(id);
        User currentUser = userService.getCurrentUser();

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own recipes");
        }

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        recipe.setTitle(request.getTitle());
        recipe.setDescription(request.getDescription());
        recipe.setInstructions(request.getInstructions());
        recipe.setPrepTime(request.getPrepTime());
        recipe.setCookTime(request.getCookTime());
        recipe.setServings(request.getServings());
        recipe.setImageUrl(request.getImageUrl());
        recipe.setCategory(category);

        return recipeRepository.save(recipe);
    }

    public List<Recipe> searchRecipes(String keyword) {
        return recipeRepository.findByTitleContainingIgnoreCase(keyword);
    }

    public List<Recipe> getRecipesByCategory(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId);
    }

    public Recipe updateRecipeVisibility(Long id, Boolean isPublic) {
        Recipe recipe = getRecipeById(id);
        User currentUser = userService.getCurrentUser();

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only update your own recipe visibility");
        }

        recipe.setIsPublic(isPublic);

        return recipeRepository.save(recipe);
    }

    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        User currentUser = userService.getCurrentUser();

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }

        recipeRepository.delete(recipe);
    }

}