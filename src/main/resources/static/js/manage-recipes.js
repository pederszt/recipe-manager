$(document).ready(function() {
    console.log("Manage Recipes JS loaded.");

    
});

function editRecipe(element) {
    let recipeId = $(element).data().id;
    $.getJSON("/recipes/" + recipeId, function(data) {
        console.log("Editing recipe:", data);
        // Implement the logic to open an edit modal or redirect to an edit page
        $("#editRecipeModal").modal('show');
        
        $("#editRecipeModal").find("#recipeId").val(data.id);
        $("#editRecipeModal").find("#recipeName").val(data.name);
        $("#editRecipeModal").find("#recipeCategory").val(data.category);
        $("#editRecipeModal").find("#numberOfMeals").val(data.numberOfMeals);
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
        success: function(response) {
            console.log("Recipe saved successfully:", response);
            $("#editRecipeModal").modal('hide');
            location.reload(); // Reload the page to reflect changes
        }
    });
}