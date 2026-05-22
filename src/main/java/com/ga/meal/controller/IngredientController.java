package com.ga.meal.controller;

import com.ga.meal.entity.Ingredient;
import com.ga.meal.service.IngredientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingredients")
@RequiredArgsConstructor
public class IngredientController {

    private final IngredientService ingredientService;

    @PostMapping
    public Ingredient createIngredient(@Valid @RequestBody Ingredient ingredient) {
        return ingredientService.createIngredient(ingredient);
    }

    @GetMapping
    public List<Ingredient> getAllIngredients() {
        return ingredientService.getAllIngredients();
    }

    @GetMapping("/{id}")
    public Ingredient getIngredientById(@PathVariable Long id) {
        return ingredientService.getIngredientById(id);
    }

    @PutMapping("/{id}")
    public Ingredient updateIngredient(
            @PathVariable Long id,
            @Valid @RequestBody Ingredient ingredient
    ) {
        return ingredientService.updateIngredient(id, ingredient);
    }

    @DeleteMapping("/{id}")
    public String deleteIngredient(@PathVariable Long id) {
        ingredientService.deleteIngredient(id);
        return "Ingredient deleted successfully";
    }
}
