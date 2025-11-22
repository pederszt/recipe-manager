$(document).ready(function () {
    console.log("Manage Recipes JS loaded.");

    let ingredientSelect = $('#editRecipeModal').find("#ingredientsSelect");
    ingredientSelect.select2({
        ajax: {
            url: '/ingredients',
            dataType: 'json',
            processResults: function (data) {
                console.log(data);
                let results =  data.map(function (ingredient) {
                        return { id: ingredient.id, 
                            
                            text: ingredient.name + " (" + ingredient.quantity + " " + ingredient.unit + ")" };
                    });
                console.log("Processed results:", results);
                return {results: results};
            }
        }
    }).on('select2:selecting', function (e) {
        console.log(e);
        console.log(e.params.args.data);
        let selectedIngredient = e.params.args.data;
        let tbody = $("#editRecipeModal").find("#ingredientsTable tbody");
        let row = `<tr>
                <td>${selectedIngredient.text}
                    <input type="hidden" name="ingredientId" value="${selectedIngredient.id}">
                </td>
                <td><input type="number" min="1" value="1" class="ingredient-count"></td>
              </tr>`;
        tbody.append(row);
        
        ingredientSelect.select2('close');
        // Do something
        return false;
        });;

});

function editRecipe(element) {
    let recipeId = $(element).data().id;
    if(!recipeId) {
        $("#editRecipeModal").modal("show");

        $("#editRecipeModal").find("#recipeId").val("");
        $("#editRecipeModal").find("#recipeName").val("");
        $("#editRecipeModal").find("#recipeCategory").val("");
        $("#editRecipeModal").find("#numberOfMeals").val("");
        
        let tbody = $("#editRecipeModal").find("#ingredientsTable tbody");
        tbody.empty();

        return;
    }
    $.getJSON("/recipes/" + recipeId, function (data) {
        console.log("Editing recipe:", data);
        // Implement the logic to open an edit modal or redirect to an edit page
        $("#editRecipeModal").modal("show");

        $("#editRecipeModal").find("#recipeId").val(data.id);
        $("#editRecipeModal").find("#recipeName").val(data.name);
        $("#editRecipeModal").find("#recipeCategory").val(data.category);
        $("#editRecipeModal").find("#numberOfMeals").val(data.numberOfMeals);

        let tbody = $("#editRecipeModal").find("#ingredientsTable tbody");
        tbody.empty();
        for (let ingredient of data.ingredients) {
            let row = `<tr>
                <td>${ingredient.name} (${ingredient.quantity} ${ingredient.unit})
                <input type="hidden" name="ingredientId" value="${ingredient.id}">
                </td>
                <td><input type="number" min="1" value="${ingredient.count}" class="ingredient-count"></td>
              </tr>`;
            tbody.append(row);
        }
    });
}

function saveRecipe() {
    let recipeData = {
        id: $("#editRecipeModal").find("#recipeId").val(),
        name: $("#editRecipeModal").find("#recipeName").val(),
        category: $("#editRecipeModal").find("#recipeCategory").val(),
        numberOfMeals: $("#editRecipeModal").find("#numberOfMeals").val(),
        ingredients: []
    };

    $("#ingredientsTable tbody tr").each((i,el)=>{
        console.log(el);
        let ingredientId = $(el).find("input[name='ingredientId']").val();
        let count = $(el).find(".ingredient-count").val();
        recipeData.ingredients.push({
            ingredientId: ingredientId,
            count: count
        });
    });

    $.ajax({
        url: "/recipes",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(recipeData),
        success: function (response) {
            console.log("Recipe saved successfully:", response);
            $("#editRecipeModal").modal('hide');
            if(recipeData.id === '') {
                // New recipe added, append to the table
                let newRow = `<tr>
                    <td>${response.name}</td>
                    <td>${response.category}</td>
                    <td>${response.numberOfMeals}</td>
                    <td>
                        <button class="btn btn-primary btn-sm" data-id="${response.id}" onclick="editRecipe(this)">Edit</button>
                    </td>
                </tr>`;
                $("#recipesTable tbody").append(newRow);
            }
        }
    });
}

function addIngredient() {
    $("#editIngredientModal").modal("show");
}

function saveIngredient() {
    let ingredientData = {
        name: $("#editIngredientModal").find("#name").val(),
        unit: $("#editIngredientModal").find("#unit").val(),
        quantity: $("#editIngredientModal").find("#quantity").val()
    };
    $.ajax({
        url: "/ingredients",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(ingredientData),
        success: function (response) {
            console.log("Ingredient saved successfully:", response);
            $("#editIngredientModal").modal('hide');
        }
    });
}