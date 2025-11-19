package org.gnorlsoft.recipemanager.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Ingredient {

    private String name;
    private String quantity;
}
