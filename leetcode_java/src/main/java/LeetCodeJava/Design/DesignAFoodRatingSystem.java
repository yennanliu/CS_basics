package LeetCodeJava.Design;

// https://leetcode.com/problems/design-a-food-rating-system/

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

/**
 *  2353. Design a Food Rating System
 *  Medium
 *
 *  Design a food rating system that can do the following:
 *   - Modify the rating of a food item listed in the system.
 *   - Return the highest-rated food item for a type of cuisine in the system.
 *
 *  Implement the FoodRatings class:
 *
 *   - FoodRatings(String[] foods, String[] cuisines, int[] ratings) Initializes the
 *     system. The food items are described by foods, cuisines and ratings, all of which
 *     have a length of n. foods[i] is the name of the ith food, cuisines[i] is the type
 *     of cuisine of the ith food, and ratings[i] is the initial rating of the ith food.
 *   - void changeRating(String food, int newRating) Changes the rating of the food item
 *     with the name food.
 *   - String highestRated(String cuisine) Returns the name of the food item that has the
 *     highest rating for the given type of cuisine. If there is a tie, return the item
 *     with the lexicographically smaller name.
 *
 *  Example 1:
 *    Input
 *      ["FoodRatings","highestRated","highestRated","changeRating","highestRated",
 *       "changeRating","highestRated"]
 *      [[["kimchi","miso","sushi","moussaka","ramen","bulgogi"],
 *        ["korean","japanese","japanese","greek","japanese","korean"],
 *        [9,12,8,15,14,7]],
 *       ["korean"],["japanese"],["sushi",16],["japanese"],["ramen",16],["japanese"]]
 *    Output
 *      [null,"kimchi","ramen",null,"sushi",null,"ramen"]
 *    Explanation
 *      after changeRating("ramen", 16) both "sushi" and "ramen" are rated 16, and
 *      "ramen" is lexicographically smaller, so it wins the tie.
 *
 *  Constraints:
 *    1 <= n <= 2 * 10^4
 *    n == foods.length == cuisines.length == ratings.length
 *    1 <= foods[i].length, cuisines[i].length <= 10
 *    foods[i], cuisines[i] consist of lowercase English letters.
 *    1 <= ratings[i] <= 10^8
 *    All the strings in foods are distinct.
 *    food will be the name of a food item in the system across all calls to changeRating.
 *    cuisine will be a type of cuisine of at least one food item in the system across
 *    all calls to highestRated.
 *    At most 2 * 10^4 calls in total will be made to changeRating and highestRated.
 */
public class DesignAFoodRatingSystem {

    // V0
    // IDEA: ONE ORDERED SET PER CUISINE, KEYED BY (-rating, name)
    //
    //   `ratingOf` is the single source of truth for a food's rating, and the
    //   per-cuisine TreeSet is ordered by a comparator that reads it: rating
    //   DESCENDING, then name ASCENDING -- exactly LeetCode's tie-break rule, so
    //   highestRated() is simply first().
    //
    //   because the comparator depends on ratingOf, changeRating must REMOVE the
    //   food from the set BEFORE overwriting the rating and re-add it after;
    //   mutating the key in place would corrupt the tree.
    /**
     * time = O(N log N) init, O(log N) per changeRating, O(log N) per highestRated
     * space = O(N)
     */
    private final Map<String, Integer> ratingOf = new HashMap<>();
    private final Map<String, String> cuisineOf = new HashMap<>();
    private final Map<String, TreeSet<String>> byCuisine = new HashMap<>();
    private final Comparator<String> cmp = new Comparator<String>() {
        @Override
        public int compare(String a, String b) {
            int ra = ratingOf.get(a);
            int rb = ratingOf.get(b);
            if (ra != rb) {
                return rb - ra;             // higher rating first
            }
            return a.compareTo(b);          // tie -> lexicographically smaller
        }
    };

    public DesignAFoodRatingSystem(String[] foods, String[] cuisines, int[] ratings) {
        for (int i = 0; i < foods.length; i++) {
            ratingOf.put(foods[i], ratings[i]);
            cuisineOf.put(foods[i], cuisines[i]);
        }
        for (int i = 0; i < foods.length; i++) {
            TreeSet<String> set = byCuisine.get(cuisines[i]);
            if (set == null) {
                set = new TreeSet<>(this.cmp);
                byCuisine.put(cuisines[i], set);
            }
            set.add(foods[i]);
        }
    }

    public void changeRating(String food, int newRating) {
        TreeSet<String> set = byCuisine.get(cuisineOf.get(food));
        set.remove(food);
        ratingOf.put(food, newRating);
        set.add(food);
    }

    public String highestRated(String cuisine) {
        return byCuisine.get(cuisine).first();
    }
}
