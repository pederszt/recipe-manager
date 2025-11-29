$(function(){

});

function generateMenu(){
    //get available recipes
    $.getJSON("/recipes", function (recipes) {
        console.log("Available recipes:", recipes);
        let menu = [];
        let totalMeals = 0;
        //select recipes until we reach 21 meals
        while(totalMeals < 7){
            let recipe = recipes[Math.floor(Math.random()*recipes.length)];
            menu.push(recipe);
            totalMeals += recipe.numberOfMeals;
        }
        console.log("Generated menu:", menu);
        //display menu
        let menuRow = $("#menuTable tbody tr");
        menuRow.empty();
        for(let recipe of menu){
            let recipeCell = `<td class="recipe-item" colspan="${recipe.numberOfMeals}">
                <h3>${recipe.name}</h3>
                <p>Category: ${recipe.category}</p>
                <p>Number of Meals: ${recipe.numberOfMeals}</p>
            </td>`;
            menuRow.append(recipeCell);
        }   
    });  
}