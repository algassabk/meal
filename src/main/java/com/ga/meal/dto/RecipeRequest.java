package com.ga.meal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeRequest {

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @Min(value = 0, message = "Prep time cannot be negative")
    private Integer prepTime;

    @Min(value = 0, message = "Cook time cannot be negative")
    private Integer cookTime;

    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings;

    private String imageUrl;

    @NotNull(message = "Category id is required")
    private Long categoryId;
}
