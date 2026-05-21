package com.ga.meal.service;

import com.ga.meal.entity.MealPlan;
import com.ga.meal.entity.MealPlanItem;
import com.ga.meal.entity.Recipe;
import com.ga.meal.repository.MealPlanItemRepository;
import com.ga.meal.repository.MealPlanRepository;
import com.ga.meal.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealPlanItemService {

    private final MealPlanItemRepository mealPlanItemRepository;
    private final MealPlanRepository mealPlanRepository;
    private final RecipeRepository recipeRepository;

    public MealPlanItem addRecipeToMealPlan(
            Long mealPlanId,
            Long recipeId,
            MealPlanItem mealPlanItem
    ) {

        MealPlan mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new RuntimeException("Meal plan not found"));

        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new RuntimeException("Recipe not found"));

        mealPlanItem.setMealPlan(mealPlan);
        mealPlanItem.setRecipe(recipe);

        return mealPlanItemRepository.save(mealPlanItem);
    }

    public List<MealPlanItem> getMealPlanItems(Long mealPlanId) {
        return mealPlanItemRepository.findByMealPlanId(mealPlanId);
    }

    public void deleteMealPlanItem(Long mealPlanItemId) {

        MealPlanItem mealPlanItem = mealPlanItemRepository.findById(mealPlanItemId)
                .orElseThrow(() -> new RuntimeException("Meal plan item not found"));

        mealPlanItemRepository.delete(mealPlanItem);
    }
}