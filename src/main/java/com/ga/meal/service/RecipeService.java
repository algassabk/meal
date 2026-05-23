package com.ga.meal.service;

import com.ga.meal.dto.RecipeRequest;
import com.ga.meal.entity.Category;
import com.ga.meal.entity.Recipe;
import com.ga.meal.entity.User;
import com.ga.meal.enums.RecipeStatus;
import com.ga.meal.repository.CategoryRepository;
import com.ga.meal.repository.CommentRepository;
import com.ga.meal.repository.FavoriteRepository;
import com.ga.meal.repository.MealPlanItemRepository;
import com.ga.meal.repository.RecipeRepository;
import com.ga.meal.repository.RecipeIngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final FavoriteRepository favoriteRepository;
    private final CommentRepository commentRepository;
    private final RecipeIngredientRepository recipeIngredientRepository;
    private final MealPlanItemRepository mealPlanItemRepository;

    public Recipe createRecipe(RecipeRequest request) {
        User currentUser = userService.getCurrentUser();

        Recipe recipe = buildRecipeFromRequest(request, currentUser);

        return recipeRepository.save(recipe);
    }

    public List<Recipe> createBulkRecipes(List<RecipeRequest> requests) {
        User currentUser = userService.getCurrentUser();
        List<Recipe> recipes = new ArrayList<>();

        for (RecipeRequest request : requests) {
            recipes.add(buildRecipeFromRequest(request, currentUser));
        }

        return recipeRepository.saveAll(recipes);
    }

    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }

    public Page<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable);
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

    public Page<Recipe> searchRecipes(String keyword, Pageable pageable) {
        return recipeRepository.findByTitleContainingIgnoreCase(keyword, pageable);
    }

    public List<Recipe> getRecipesByCategory(Long categoryId) {
        return recipeRepository.findByCategoryId(categoryId);
    }

    public Page<Recipe> getRecipesByCategory(Long categoryId, Pageable pageable) {
        return recipeRepository.findByCategoryId(categoryId, pageable);
    }

    public List<Recipe> getPendingRecipes() {
        return recipeRepository.findAll()
                .stream()
                .filter(recipe -> recipe.getStatus() == RecipeStatus.PENDING)
                .toList();
    }

    public Recipe approveRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        recipe.setStatus(RecipeStatus.APPROVED);
        return recipeRepository.save(recipe);
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

    public Recipe uploadRecipeImage(Long id, MultipartFile file) {
        Recipe recipe = getRecipeById(id);
        User currentUser = userService.getCurrentUser();

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only upload images for your own recipes");
        }

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
            throw new RuntimeException("Could not upload image");
        }

        recipe.setImageUrl("/uploads/" + fileName);
        return recipeRepository.save(recipe);
    }

    @Transactional
    public void deleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        User currentUser = userService.getCurrentUser();

        if (!recipe.getUser().getId().equals(currentUser.getId())) {
            throw new RuntimeException("You can only delete your own recipes");
        }

        deleteRecipeLinks(id);
        recipeRepository.delete(recipe);
    }

    @Transactional
    public void adminDeleteRecipe(Long id) {
        Recipe recipe = getRecipeById(id);
        deleteRecipeLinks(id);
        recipeRepository.delete(recipe);
    }

    private void deleteRecipeLinks(Long recipeId) {
        favoriteRepository.deleteByRecipeId(recipeId);
        commentRepository.deleteByRecipeId(recipeId);
        recipeIngredientRepository.deleteByRecipeId(recipeId);
        mealPlanItemRepository.deleteByRecipeId(recipeId);
    }

    private Recipe buildRecipeFromRequest(RecipeRequest request, User user) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return Recipe.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .instructions(request.getInstructions())
                .prepTime(request.getPrepTime())
                .cookTime(request.getCookTime())
                .servings(request.getServings())
                .imageUrl(request.getImageUrl())
                .status(RecipeStatus.PENDING)
                .user(user)
                .category(category)
                .build();
    }
}
