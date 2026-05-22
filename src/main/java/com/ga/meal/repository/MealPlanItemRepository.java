package com.ga.meal.repository;

import com.ga.meal.entity.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {

    List<MealPlanItem> findByMealPlanId(Long mealPlanId);

    void deleteByRecipeId(Long recipeId);
}
