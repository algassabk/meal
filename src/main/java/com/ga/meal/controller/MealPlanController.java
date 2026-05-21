package com.ga.meal.controller;

import com.ga.meal.entity.MealPlan;
import com.ga.meal.service.MealPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/meal-plans")
@RequiredArgsConstructor
public class MealPlanController {

    private final MealPlanService mealPlanService;

    @PostMapping
    public MealPlan createMealPlan(@RequestBody MealPlan mealPlan) {
        return mealPlanService.createMealPlan(mealPlan);
    }

    @GetMapping
    public List<MealPlan> getMyMealPlans() {
        return mealPlanService.getMyMealPlans();
    }

    @GetMapping("/{id}")
    public MealPlan getMealPlanById(@PathVariable Long id) {
        return mealPlanService.getMealPlanById(id);
    }

    @PutMapping("/{id}")
    public MealPlan updateMealPlan(
            @PathVariable Long id,
            @RequestBody MealPlan mealPlan
    ) {
        return mealPlanService.updateMealPlan(id, mealPlan);
    }

    @DeleteMapping("/{id}")
    public String deleteMealPlan(@PathVariable Long id) {
        mealPlanService.deleteMealPlan(id);
        return "Meal plan deleted successfully";
    }
}