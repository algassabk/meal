package com.ga.meal.controller;

import com.ga.meal.entity.RecipeIngredient;
import com.ga.meal.service.RecipeIngredientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/{recipeId}/ingredients")
@RequiredArgsConstructor
public class RecipeIngredientController {

    private final RecipeIngredientService recipeIngredientService;

    @PostMapping("/{ingredientId}")
    public RecipeIngredient addIngredientToRecipe(
            @PathVariable Long recipeId,
            @PathVariable Long ingredientId,
            @RequestBody RecipeIngredient recipeIngredient
    ) {
        return recipeIngredientService.addIngredientToRecipe(recipeId, ingredientId, recipeIngredient);
    }

    @GetMapping
    public List<RecipeIngredient> getRecipeIngredients(@PathVariable Long recipeId) {
        return recipeIngredientService.getRecipeIngredients(recipeId);
    }

    @PutMapping("/{recipeIngredientId}/{ingredientId}")
    public RecipeIngredient updateRecipeIngredient(
            @PathVariable Long recipeId,
            @PathVariable Long recipeIngredientId,
            @PathVariable Long ingredientId,
            @RequestBody RecipeIngredient recipeIngredient
    ) {
        return recipeIngredientService.updateRecipeIngredient(recipeId, recipeIngredientId, ingredientId, recipeIngredient);
    }

    @DeleteMapping("/{recipeIngredientId}")
    public String deleteRecipeIngredient(
            @PathVariable Long recipeId,
            @PathVariable Long recipeIngredientId
    ) {
        recipeIngredientService.deleteRecipeIngredient(recipeId, recipeIngredientId);
        return "Recipe ingredient deleted successfully";
    }
}