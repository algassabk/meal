package com.ga.meal.repository;

import com.ga.meal.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    List<Recipe> findByTitleContainingIgnoreCase(String title);

    List<Recipe> findByCategoryId(Long categoryId);
}