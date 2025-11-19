package org.gnorlsoft.recipemanager.service;

import java.util.List;

import org.gnorlsoft.recipemanager.entities.Recipe;
import org.gnorlsoft.recipemanager.repository.RecipeRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@RequiredArgsConstructor
@Service
@CommonsLog
public class RecipeService {

    private final RecipeRepository recipeRepository;

    public List<Recipe> getAllRecipes() {
        return recipeRepository.getRecipes();
    }

    public Recipe getRecipeById(int id) {
        return recipeRepository.getRecipeById(id);
    }

    public void saveRecipe(Recipe recipe) {
        // Implementation to save a recipe
        // This is a placeholder; actual implementation would involve saving to a database or file
        log.info("Saving recipe: " + recipe.getName());
        recipeRepository.saveRecipe(recipe);
    }
}
