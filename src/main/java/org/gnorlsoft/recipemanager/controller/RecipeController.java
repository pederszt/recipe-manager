package org.gnorlsoft.recipemanager.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.gnorlsoft.recipemanager.dto.RecipeDto;
import org.gnorlsoft.recipemanager.entities.Ingredient;
import org.gnorlsoft.recipemanager.entities.Recipe;
import org.gnorlsoft.recipemanager.service.RecipeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@RestController
public class RecipeController {

    private final RecipeService recipeService;

    @GetMapping("manage-recipes")
    public ModelAndView getMethodName() {

        Map<String, Object> model = new HashMap<>();
        model.put("recipes", recipeService.getAllRecipes());

        return new ModelAndView("manage-recipes", model);
    }

    @GetMapping("/recipes/{id}")
    public RecipeDto getRecipe(@PathVariable("id") Integer id) {
        return recipeService.getRecipeById(id);
    }

    @PostMapping("/recipes")
    public void saveRecipe(@RequestBody Recipe entity) {
        recipeService.saveRecipe(entity);
    }

    @GetMapping("/ingredients")
    public List<Ingredient> findIngredients(@RequestParam("q") String q) {
        return recipeService.findIngredientsByName(q);
    }

     @PostMapping("/ingredients")
    public void saveIngredient(@RequestBody Ingredient entity) {
        recipeService.saveIngredient(entity);
    }
}
