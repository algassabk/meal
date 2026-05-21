package com.ga.meal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecipeIngredientRequest {

    private Long ingredientId;
    private Double quantity;
    private String unit;
}