package org.gnorlsoft.recipemanager.repository;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.gnorlsoft.recipemanager.entities.Ingredient;
import org.gnorlsoft.recipemanager.entities.Recipe;
import org.gnorlsoft.recipemanager.exception.NotFoundException;
import org.gnorlsoft.recipemanager.service.RecipeService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;

@RequiredArgsConstructor
@Repository
@CommonsLog
public class RecipeRepository {

    private final ObjectMapper objectMapper;

    private List<Recipe> recipes;
    private List<Ingredient> ingredients;

    public List<Recipe> getRecipes() {
        if (recipes == null) {
            recipes = new ArrayList<>();
            recipes.addAll(Arrays.asList(
                    loadFromFile("recipes.json", Recipe[].class))
            );
        }
        return recipes;
    }

    public List<Ingredient> getIngredients() {
        if (ingredients == null) {
            ingredients = new ArrayList<>();
            ingredients.addAll(Arrays.asList(
                    loadFromFile("ingredients.json", Ingredient[].class))
            );

            AtomicInteger counter = new AtomicInteger(0);
            ingredients.forEach(ing -> {
                log.info("Loaded ingredient: " + ing.getName());
                ing.setId(counter.incrementAndGet());
            });

            try {
                System.out.println(objectMapper.writeValueAsString(ingredients));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ingredients;
    }

    public List<Ingredient> findIngredientsByName(String name) {
        List<Ingredient> result = new ArrayList<>();
        for (Ingredient ingredient : getIngredients()) {
            if (ingredient.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(ingredient);
            }
        }
        return result;
    }

    public Ingredient findIngredientById(int id) {
        return getIngredients().stream()
                .filter(ingredient -> ingredient.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Ingredient not found with id: " + id));
    }

    private <T> T loadFromFile(String filename, Class<T> clazz) {
        // Implementation to load recipes from a file
        try (InputStream is = RecipeService.class.getClassLoader().getResourceAsStream("data/" + filename)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found: data/" + filename);
            }
            return objectMapper.readValue(is, clazz);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading resource: data/" + filename, e);
        }
    }

    public Recipe getRecipeById(int id) {
        return getRecipes().stream()
                .filter(recipe -> recipe.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Recipe not found with id: " + id));
    }

    public void saveIngredient(Ingredient ingredient) {
        // Implementation to save an ingredient
        // This is a placeholder; actual implementation would involve saving to a database or file
        if (ingredient.getId() == null) {
            int newId = getIngredients().stream()
                    .mapToInt(Ingredient::getId)
                    .max()
                    .orElse(0) + 1;
            ingredient.setId(newId);
            getIngredients().add(ingredient);
        } else {
            Ingredient existingIngredient = findIngredientById(ingredient.getId());

            if (existingIngredient == null) {
                throw new NotFoundException("Ingredient not found with id: " + ingredient.getId());
            }

            BeanUtils.copyProperties(ingredient, existingIngredient);
        }

        log.info("Saved ingredient: " + ingredient.getName());
    }

    public void saveRecipe(Recipe recipe) {
        // Implementation to save a recipe
        // This is a placeholder; actual implementation would involve saving to a database or file
        if (recipe.getId() == null) {
            int newId = getRecipes().stream()
                    .mapToInt(Recipe::getId)
                    .max()
                    .orElse(0) + 1;
            recipe.setId(newId);
            getRecipes().add(recipe);
        } else {
            Recipe existingRecipe = getRecipeById(recipe.getId());

            if (existingRecipe == null) {
                throw new NotFoundException("Recipe not found with id: " + recipe.getId());
            }

            BeanUtils.copyProperties(recipe, existingRecipe);
        }

        log.info("Saved recipe: " + recipe.getName());

        try {
            //write out new state of recipes for debugging and saving updated data model
            log.info(objectMapper.writeValueAsString(getRecipes()));
        } catch (JsonProcessingException ex) {
            log.error("Error writing recipes to JSON", ex);
        }
    }
}
