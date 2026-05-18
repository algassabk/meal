package com.ga.meal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeRequest {

    private String title;
    private String description;
    private String instructions;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String imageUrl;
    private Long categoryId;
}