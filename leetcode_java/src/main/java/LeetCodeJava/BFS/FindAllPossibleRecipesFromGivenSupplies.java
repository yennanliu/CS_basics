package LeetCodeJava.BFS;

// https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/description/
// https://leetcode.cn/problems/find-all-possible-recipes-from-given-supplies/description/

import java.util.*;

/**
 * 2115. Find All Possible Recipes from Given Supplies
 * Medium
 * Topics
 * premium lock icon
 * Companies
 * Hint
 * You have information about n different recipes. You are given a string array recipes and a 2D string array ingredients. The ith recipe has the name recipes[i], and you can create it if you have all the needed ingredients from ingredients[i]. A recipe can also be an ingredient for other recipes, i.e., ingredients[i] may contain a string that is in recipes.
 *
 * You are also given a string array supplies containing all the ingredients that you initially have, and you have an infinite supply of all of them.
 *
 * Return a list of all the recipes that you can create. You may return the answer in any order.
 *
 * Note that two recipes may contain each other in their ingredients.
 *
 *
 *
 * Example 1:
 *
 * Input: recipes = ["bread"], ingredients = [["yeast","flour"]], supplies = ["yeast","flour","corn"]
 * Output: ["bread"]
 * Explanation:
 * We can create "bread" since we have the ingredients "yeast" and "flour".
 * Example 2:
 *
 * Input: recipes = ["bread","sandwich"], ingredients = [["yeast","flour"],["bread","meat"]], supplies = ["yeast","flour","meat"]
 * Output: ["bread","sandwich"]
 * Explanation:
 * We can create "bread" since we have the ingredients "yeast" and "flour".
 * We can create "sandwich" since we have the ingredient "meat" and can create the ingredient "bread".
 * Example 3:
 *
 * Input: recipes = ["bread","sandwich","burger"], ingredients = [["yeast","flour"],["bread","meat"],["sandwich","meat","bread"]], supplies = ["yeast","flour","meat"]
 * Output: ["bread","sandwich","burger"]
 * Explanation:
 * We can create "bread" since we have the ingredients "yeast" and "flour".
 * We can create "sandwich" since we have the ingredient "meat" and can create the ingredient "bread".
 * We can create "burger" since we have the ingredient "meat" and can create the ingredients "bread" and "sandwich".
 *
 *
 * Constraints:
 *
 * n == recipes.length == ingredients.length
 * 1 <= n <= 100
 * 1 <= ingredients[i].length, supplies.length <= 100
 * 1 <= recipes[i].length, ingredients[i][j].length, supplies[k].length <= 10
 * recipes[i], ingredients[i][j], and supplies[k] consist only of lowercase English letters.
 * All the values of recipes and supplies combined are unique.
 * Each ingredients[i] does not contain any duplicate values.
 *
 *
 */
public class FindAllPossibleRecipesFromGivenSupplies {

    // V0
//    public List<String> findAllRecipes(String[] recipes, List<List<String>> ingredients, String[] supplies) {
//
//    }

    // V1-1
    // https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/editorial/
    // IDEA: BFS
    public List<String> findAllRecipes_1_1(
            String[] recipes,
            List<List<String>> ingredients,
            String[] supplies) {
        // Track available ingredients and recipes
        Set<String> available = new HashSet<>();
        for (String supply : supplies) {
            available.add(supply);
        }

        // Queue to process recipe indices
        Queue<Integer> recipeQueue = new LinkedList<>();
        for (int idx = 0; idx < recipes.length; ++idx) {
            recipeQueue.offer(idx);
        }

        List<String> createdRecipes = new ArrayList<>();
        int lastSize = -1;

        // Continue while we keep finding new recipes
        while (available.size() > lastSize) {
            lastSize = available.size();
            int queueSize = recipeQueue.size();

            // Process all recipes in current queue
            while (queueSize-- > 0) {
                int recipeIdx = recipeQueue.poll();
                boolean canCreate = true;

                // Check if all ingredients are available
                for (String ingredient : ingredients.get(recipeIdx)) {
                    if (!available.contains(ingredient)) {
                        canCreate = false;
                        break;
                    }
                }

                if (!canCreate) {
                    recipeQueue.offer(recipeIdx);
                } else {
                    // Recipe can be created - add to available items
                    available.add(recipes[recipeIdx]);
                    createdRecipes.add(recipes[recipeIdx]);
                }
            }
        }

        return createdRecipes;
    }


    // V1-2
    // https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/editorial/
    // IDEA: DFS
    public List<String> findAllRecipes_1_2(
            String[] recipes,
            List<List<String>> ingredients,
            String[] supplies) {
        List<String> possibleRecipes = new ArrayList<>();
        // Track if ingredient/recipe can be made
        Map<String, Boolean> canMake = new HashMap<>();
        // Map recipe name to its index in ingredients list
        Map<String, Integer> recipeToIndex = new HashMap<>();

        // Mark all initial supplies as available
        for (String supply : supplies) {
            canMake.put(supply, true);
        }

        // Create recipe to index mapping
        for (int idx = 0; idx < recipes.length; idx++) {
            recipeToIndex.put(recipes[idx], idx);
        }

        // Try to make each recipe using DFS
        for (String recipe : recipes) {
            checkRecipe(
                    recipe,
                    ingredients,
                    new HashSet<String>(),
                    canMake,
                    recipeToIndex);
            if (canMake.get(recipe)) {
                possibleRecipes.add(recipe);
            }
        }

        return possibleRecipes;
    }

    private void checkRecipe(
            String recipe,
            List<List<String>> ingredients,
            Set<String> visited,
            Map<String, Boolean> canMake,
            Map<String, Integer> recipeToIndex) {
        // Return if we already know if recipe can be made
        if (canMake.containsKey(recipe) && canMake.get(recipe)) {
            return;
        }

        // Not a valid recipe or cycle detected
        if (!recipeToIndex.containsKey(recipe) || visited.contains(recipe)) {
            canMake.put(recipe, false);
            return;
        }

        visited.add(recipe);

        // Check if we can make all required ingredients
        List<String> neededIngredients = ingredients.get(
                recipeToIndex.get(recipe));
        for (String ingredient : neededIngredients) {
            checkRecipe(
                    ingredient,
                    ingredients,
                    visited,
                    canMake,
                    recipeToIndex);
            if (!canMake.get(ingredient)) {
                canMake.put(recipe, false);
                return;
            }
        }

        // All ingredients can be made
        canMake.put(recipe, true);
    }


    // V1-3
    // https://leetcode.com/problems/find-all-possible-recipes-from-given-supplies/editorial/
    // IDEA: Topological Sort (Kahn's Algorithm)
    public List<String> findAllRecipes_1_3(
            String[] recipes,
            List<List<String>> ingredients,
            String[] supplies) {
        // Store available supplies
        Set<String> availableSupplies = new HashSet<>();
        // Map recipe to its index
        Map<String, Integer> recipeToIndex = new HashMap<>();
        // Map ingredient to recipes that need it
        Map<String, List<String>> dependencyGraph = new HashMap<>();

        // Initialize available supplies
        for (String supply : supplies) {
            availableSupplies.add(supply);
        }

        // Create recipe to index mapping
        for (int idx = 0; idx < recipes.length; idx++) {
            recipeToIndex.put(recipes[idx], idx);
        }

        // Count of non-supply ingredients needed for each recipe
        int[] inDegree = new int[recipes.length];

        // Build dependency graph
        for (int recipeIdx = 0; recipeIdx < recipes.length; recipeIdx++) {
            for (String ingredient : ingredients.get(recipeIdx)) {
                if (!availableSupplies.contains(ingredient)) {
                    // Add edge: ingredient -> recipe
                    dependencyGraph.putIfAbsent(
                            ingredient,
                            new ArrayList<String>());
                    dependencyGraph.get(ingredient).add(recipes[recipeIdx]);
                    inDegree[recipeIdx]++;
                }
            }
        }

        // Start with recipes that only need supplies
        Queue<Integer> queue = new LinkedList<>();
        for (int recipeIdx = 0; recipeIdx < recipes.length; recipeIdx++) {
            if (inDegree[recipeIdx] == 0) {
                queue.add(recipeIdx);
            }
        }

        // Process recipes in topological order
        List<String> createdRecipes = new ArrayList<>();
        while (!queue.isEmpty()) {
            int recipeIdx = queue.poll();
            String recipe = recipes[recipeIdx];
            createdRecipes.add(recipe);

            // Skip if no recipes depend on this one
            if (!dependencyGraph.containsKey(recipe))
                continue;

            // Update recipes that depend on current recipe
            for (String dependentRecipe : dependencyGraph.get(recipe)) {
                if (--inDegree[recipeToIndex.get(dependentRecipe)] == 0) {
                    queue.add(recipeToIndex.get(dependentRecipe));
                }
            }
        }

        return createdRecipes;
    }


    // V2

}
