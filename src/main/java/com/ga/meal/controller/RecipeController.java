package com.ga.meal.controller;

import com.ga.meal.dto.RecipeRequest;
import com.ga.meal.entity.Recipe;
import com.ga.meal.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public Recipe createRecipe(@RequestBody RecipeRequest request) {
        return recipeService.createRecipe(request);
    }

    @GetMapping
    public List<Recipe> getAllRecipes() {
        return recipeService.getAllRecipes();
    }

    @GetMapping("/{id}")
    public Recipe getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @PutMapping("/{id}")
    public Recipe updateRecipe(
            @PathVariable Long id,
            @RequestBody RecipeRequest request
    ) {
        return recipeService.updateRecipe(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteRecipe(@PathVariable Long id) {
        recipeService.deleteRecipe(id);
        return "Recipe deleted successfully";
    }

    @GetMapping("/search")
    public List<Recipe> searchRecipes(@RequestParam String keyword) {
        return recipeService.searchRecipes(keyword);
    }
}