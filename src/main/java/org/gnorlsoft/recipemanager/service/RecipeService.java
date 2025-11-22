package org.gnorlsoft.recipemanager.service;

import java.util.List;
import java.util.Optional;

import org.gnorlsoft.recipemanager.dto.RecipeDto;
import org.gnorlsoft.recipemanager.dto.RecipeIngredientDto;
import org.gnorlsoft.recipemanager.entities.Ingredient;
import org.gnorlsoft.recipemanager.entities.Recipe;
import org.gnorlsoft.recipemanager.entities.RecipeIngredient;
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

    public RecipeDto getRecipeById(int id) {
        Recipe recipe = recipeRepository.getRecipeById(id);
        return map(recipe);
    }

    public List<Ingredient> getIngredients() {
        return recipeRepository.getIngredients();
    }

    public List<Ingredient> findIngredientsByName(String name) {
        return recipeRepository.findIngredientsByName(name);
    }

    public void saveRecipe(Recipe recipe) {
        // Implementation to save a recipe
        // This is a placeholder; actual implementation would involve saving to a database or file
        log.info("Saving recipe: " + recipe.getName());
        recipeRepository.saveRecipe(recipe);
    }

    public void saveIngredient(Ingredient ingredient) {
        // Implementation to save an ingredient
        log.info("Saving ingredient: " + ingredient.getName());
        recipeRepository.saveIngredient(ingredient);
    }

    public RecipeDto map(Recipe recipe) {
        RecipeDto dto = new RecipeDto();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setCategory(recipe.getCategory());
        dto.setNumberOfMeals(recipe.getNumberOfMeals());

        List<RecipeIngredientDto> ingredients = Optional.ofNullable(recipe.getIngredients()).orElse(List.of()).stream().map((RecipeIngredient recipeIngredient) -> {
            recipeIngredient.getIngredientId();

            var ri = new RecipeIngredientDto();
            Ingredient ingr = recipeRepository.findIngredientById(recipeIngredient.getIngredientId());
            ri.setId(ingr.getId());
            ri.setName(ingr.getName());
            ri.setQuantity(ingr.getQuantity());
            ri.setUnit(ingr.getUnit());
            ri.setCount(recipeIngredient.getCount());
            return ri;
        }).toList();

        dto.setIngredients(ingredients);

        return dto;
    }
}
