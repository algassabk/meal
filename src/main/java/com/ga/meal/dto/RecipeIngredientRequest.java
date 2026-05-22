package com.ga.meal.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientRequest {

    @NotNull(message = "Ingredient id is required")
    private Long ingredientId;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Double quantity;

    private String unit;
}
