package org.gnorlsoft.recipemanager.dto;

import lombok.Data;

@Data
public class RecipeIngredientDto {
    private String name;
    private Double quantity;
    private String unit;
    private Integer count;
}
