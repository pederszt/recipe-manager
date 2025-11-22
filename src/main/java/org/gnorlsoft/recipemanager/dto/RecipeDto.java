package org.gnorlsoft.recipemanager.dto;

import java.util.List;

import lombok.Data;

@Data
public class RecipeDto {
    private Integer id;
    private String name;
    private String category;
    private Integer numberOfMeals;
    private List<RecipeIngredientDto> ingredients;
}
