package org.gnorlsoft.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RecipeIngredient {

    private Integer ingredientId;
    private Integer count;
}
