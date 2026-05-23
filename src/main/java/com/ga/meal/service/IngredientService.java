package com.ga.meal.service;

import com.ga.meal.entity.Ingredient;
import com.ga.meal.repository.IngredientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class IngredientService {

    private final IngredientRepository ingredientRepository;

    public Ingredient createIngredient(Ingredient ingredient) {
        return ingredientRepository.save(ingredient);
    }

    public List<Ingredient> createBulkIngredients(List<Ingredient> ingredients) {
        List<Ingredient> ingredientsToSave = new ArrayList<>();
        Set<String> namesInRequest = new HashSet<>();

        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName() == null || ingredient.getName().trim().isEmpty()) {
                continue;
            }

            String cleanName = ingredient.getName().trim();
            String lowercaseName = cleanName.toLowerCase();

            if (namesInRequest.contains(lowercaseName)) {
                continue;
            }

            if (ingredientRepository.existsByNameIgnoreCase(cleanName)) {
                continue;
            }

            ingredient.setName(cleanName);
            ingredientsToSave.add(ingredient);
            namesInRequest.add(lowercaseName);
        }

        return ingredientRepository.saveAll(ingredientsToSave);
    }

    public List<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }

    public Ingredient getIngredientById(Long id) {
        return ingredientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingredient not found"));
    }

    public Ingredient updateIngredient(Long id, Ingredient updatedIngredient) {
        Ingredient ingredient = getIngredientById(id);

        ingredient.setName(updatedIngredient.getName());
        ingredient.setUnit(updatedIngredient.getUnit());

        return ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(Long id) {
        Ingredient ingredient = getIngredientById(id);
        ingredientRepository.delete(ingredient);
    }
}
