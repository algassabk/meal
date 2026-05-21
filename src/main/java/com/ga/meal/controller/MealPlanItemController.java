package com.ga.meal.controller;

import com.ga.meal.entity.MealPlanItem;
import com.ga.meal.service.MealPlanItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-plan-items")
@RequiredArgsConstructor
public class MealPlanItemController {

    private final MealPlanItemService mealPlanItemService;

    @PostMapping("/{mealPlanId}/{recipeId}")
    public MealPlanItem addRecipeToMealPlan(
            @PathVariable Long mealPlanId,
            @PathVariable Long recipeId,
            @RequestBody MealPlanItem mealPlanItem
    ) {
        return mealPlanItemService.addRecipeToMealPlan(
                mealPlanId,
                recipeId,
                mealPlanItem
        );
    }

    @GetMapping("/{mealPlanId}")
    public List<MealPlanItem> getMealPlanItems(@PathVariable Long mealPlanId) {
        return mealPlanItemService.getMealPlanItems(mealPlanId);
    }

    @DeleteMapping("/{mealPlanItemId}")
    public String deleteMealPlanItem(@PathVariable Long mealPlanItemId) {
        mealPlanItemService.deleteMealPlanItem(mealPlanItemId);
        return "Meal plan item deleted successfully";
    }
}