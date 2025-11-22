$(document).ready(function () {
    console.log("Manage Recipes JS loaded.");


    $('#editRecipeModal').find("#ingredientsSelect").select2({
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
                <td>${selectedIngredient.text}</td>
                <td>1</td>
              </tr>`;
        tbody.append(row);
        
        // Do something
        return false;
        });;

});

function editRecipe(element) {
    let recipeId = $(element).data().id;
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
        console.log(tbody);
        for (let ingredient of data.ingredients) {
            let row = `<tr>
                <td>${ingredient.name} (${ingredient.quantity} ${ingredient.unit})</td>
                <td>${ingredient.count}</td>
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
        numberOfMeals: $("#editRecipeModal").find("#numberOfMeals").val()
    };

    $.ajax({
        url: "/recipes",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(recipeData),
        success: function (response) {
            console.log("Recipe saved successfully:", response);
            $("#editRecipeModal").modal('hide');
            location.reload(); // Reload the page to reflect changes
        }
    });
}