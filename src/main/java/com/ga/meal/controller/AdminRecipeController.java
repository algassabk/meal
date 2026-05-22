package com.ga.meal.controller;

import com.ga.meal.entity.Recipe;
import com.ga.meal.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/recipes")
@RequiredArgsConstructor
public class AdminRecipeController {

    private final RecipeService recipeService;

    @GetMapping("/pending")
    public List<Recipe> getPendingRecipes() {
        return recipeService.getPendingRecipes();
    }

    @PutMapping("/{id}/approve")
    public Recipe approveRecipe(@PathVariable Long id) {
        return recipeService.approveRecipe(id);
    }

    @DeleteMapping("/{id}")
    public String deleteAnyRecipe(@PathVariable Long id) {
        recipeService.adminDeleteRecipe(id);
        return "Recipe deleted by admin successfully";
    }
}
