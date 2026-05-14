package uni.fmi.masters.jade;

import java.util.ArrayList;

public class TestOntology {
    public static void main(String[] args) {
        System.out.println("--- Стартиране на тест на онтологията ---");

        // 1. Инициализираме онтологията
        // Увери се, че пътят в конструктора на RecipeOntology е правилен ("src/files/mydietassistant.rdf")
        RecipeOntology testOnto = new RecipeOntology();

        // 2. Дефинираме състояние за търсене
        // ЗАМЕНИ "Anemia" с име на клас от твоята онтология, ако е различно
        String conditionToTest = "Obesity";

        System.out.println("Търсене на рецепти за: " + conditionToTest);

        // 3. Извикваме метода, който написахме по модела на пиците
        ArrayList<Recipe> foundRecipes = testOnto.getRecipesByCondition(conditionToTest);

        // 4. Проверка на резултатите
        if (foundRecipes.isEmpty()) {
            System.out.println("❌ Не бяха намерени рецепти. Провери:");
            System.out.println(" - Дали името '" + conditionToTest + "' съвпада точно с класа в Protege.");
            System.out.println(" - Дали рецептите са наследници на 'NamedRecipe'.");
            System.out.println(" - Дали има аксиома 'suitedForCondition some " + conditionToTest + "'.");
        } else {
            System.out.println("✅ Намерени рецепти: " + foundRecipes.size());
            for (Recipe r : foundRecipes) {
                System.out.println("------------------------------------");
                System.out.println(r.toString());
            }
        }
    }
}
