package com.ga.meal.service;

import com.ga.meal.entity.Ingredient;
import com.ga.meal.entity.Recipe;
import com.ga.meal.entity.RecipeIngredient;
import com.ga.meal.repository.IngredientRepository;
import com.ga.meal.repository.RecipeIngredientRepository;
import com.ga.meal.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecipeIngredientService {

    private final RecipeIngredientRepository recipeIngredientRepository;
    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeIngredient addIngredientToRecipe(Long recipeId, Long ingredientId, RecipeIngredient recipeIngredient) {

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        recipeIngredient.setRecipe(recipe);
        recipeIngredient.setIngredient(ingredient);

        return recipeIngredientRepository.save(recipeIngredient);
    }

    public List<RecipeIngredient> getRecipeIngredients(Long recipeId) {
        return recipeIngredientRepository.findByRecipeId(recipeId);
    }

    public RecipeIngredient updateRecipeIngredient(Long recipeId, Long recipeIngredientId, Long ingredientId, RecipeIngredient updatedRecipeIngredient) {

        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(recipeIngredientId)
                .orElseThrow(() -> new RuntimeException("Recipe ingredient not found"));

        Ingredient ingredient = ingredientRepository.findById(ingredientId)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));

        recipeIngredient.setIngredient(ingredient);
        recipeIngredient.setQuantity(updatedRecipeIngredient.getQuantity());

        return recipeIngredientRepository.save(recipeIngredient);
    }

    public void deleteRecipeIngredient(Long recipeId, Long recipeIngredientId) {

        RecipeIngredient recipeIngredient = recipeIngredientRepository.findById(recipeIngredientId)
                .orElseThrow(() -> new RuntimeException("Recipe ingredient not found"));

        recipeIngredientRepository.delete(recipeIngredient);
    }
}