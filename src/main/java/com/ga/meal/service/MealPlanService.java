package com.ga.meal.service;

import com.ga.meal.entity.MealPlan;
import com.ga.meal.entity.User;
import com.ga.meal.repository.MealPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MealPlanService {

    private final MealPlanRepository mealPlanRepository;
    private final UserService userService;

    public MealPlan createMealPlan(MealPlan mealPlan) {
        User currentUser = userService.getCurrentUser();

        mealPlan.setUser(currentUser);

        return mealPlanRepository.save(mealPlan);
    }

    public List<MealPlan> getMyMealPlans() {
        User currentUser = userService.getCurrentUser();

        return mealPlanRepository.findByUserId(currentUser.getId());
    }

    public MealPlan getMealPlanById(Long id) {
        return mealPlanRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meal plan not found"));
    }

    public MealPlan updateMealPlan(Long id, MealPlan updatedMealPlan) {
        MealPlan mealPlan = getMealPlanById(id);

        mealPlan.setTitle(updatedMealPlan.getTitle());
        mealPlan.setStartDate(updatedMealPlan.getStartDate());
        mealPlan.setEndDate(updatedMealPlan.getEndDate());

        return mealPlanRepository.save(mealPlan);
    }

    public void deleteMealPlan(Long id) {
        MealPlan mealPlan = getMealPlanById(id);

        mealPlanRepository.delete(mealPlan);
    }
}