package LeetCodeJava.Math;

// https://leetcode.com/problems/number-of-burgers-with-no-waste-of-ingredients/description/

import java.util.*;

/**
 * 1276. Number of Burgers with No Waste of Ingredients
 * Medium
 *
 * Given two integers tomatoSlices and cheeseSlices. The ingredients of different burgers are as
 * follows:
 *
 * Jumbo Burger: 4 tomato slices and 1 cheese slice.
 * Small Burger: 2 Tomato slices and 1 cheese slice.
 *
 * Return [total_jumbo, total_small] so that the number of remaining tomatoSlices equal to 0 and
 * the number of remaining cheeseSlices equal to 0. If it is not possible to make the remaining
 * tomatoSlices and cheeseSlices equal to 0 return [].
 *
 *
 * Example 1:
 *
 * Input: tomatoSlices = 16, cheeseSlices = 7
 * Output: [1,6]
 * Explantion: To make one jumbo burger and 6 small burgers we need 4*1 + 2*6 = 16 tomato and
 * 1 + 6 = 7 cheese. There will be no remaining ingredients.
 *
 * Example 2:
 *
 * Input: tomatoSlices = 17, cheeseSlices = 4
 * Output: []
 * Explantion: There will be no way to use all ingredients to make small and jumbo burgers.
 *
 * Example 3:
 *
 * Input: tomatoSlices = 4, cheeseSlices = 17
 * Output: []
 * Explantion: Making 1 jumbo burger there will be 16 cheese remaining and making 2 small burgers
 * there will be 15 cheese remaining.
 *
 *
 * Constraints:
 *
 * 0 <= tomatoSlices, cheeseSlices <= 10^7
 *
 */
public class NumberOfBurgersWithNoWasteOfIngredients {

    // V0
    // IDEA: SOLVE THE 2 x 2 LINEAR SYSTEM
    //   4 * jumbo + 2 * small = tomatoSlices
    //       jumbo +     small = cheeseSlices
    //   -> jumbo = tomatoSlices / 2 - cheeseSlices
    //      small = 2 * cheeseSlices - tomatoSlices / 2
    //   valid only when tomatoSlices is even and both counts are >= 0.
    /**
     * time = O(1)
     * space = O(1)
     */
    public List<Integer> numOfBurgers(int tomatoSlices, int cheeseSlices) {
        // every burger uses an even number of tomato slices
        if (tomatoSlices % 2 != 0) {
            return new ArrayList<>();
        }
        int halfTomato = tomatoSlices / 2;
        int jumbo = halfTomato - cheeseSlices;
        int small = 2 * cheeseSlices - halfTomato;
        if (jumbo < 0 || small < 0) {
            return new ArrayList<>();
        }
        return Arrays.asList(jumbo, small);
    }
}
