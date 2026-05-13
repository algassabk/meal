package com.ga.meal.repository;

import com.ga.meal.entity.MealPlanItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealPlanItemRepository extends JpaRepository<MealPlanItem, Long> {
}