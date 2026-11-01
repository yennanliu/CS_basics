package LeetCodeJava.HashTable;

// https://leetcode.com/problems/count-good-meals/description/

import java.util.*;

/**
 * 1711. Count Good Meals
 * Medium
 *
 * A good meal is a meal that contains exactly two different food items with a sum of deliciousness
 * equal to a power of two.
 *
 * You can pick any two different foods to make a good meal.
 *
 * Given an array of integers deliciousness where deliciousness[i] is the deliciousness of the ith
 * item of food, return the number of different good meals you can make from this list modulo 10^9 + 7.
 *
 * Note that items with different indices are considered different even if they have the same
 * deliciousness value.
 *
 *
 * Example 1:
 *
 * Input: deliciousness = [1,3,5,7,9]
 * Output: 4
 * Explanation: The good meals are (1,3), (1,7), (3,5) and, (7,9).
 * Their respective sums are 4, 8, 8, and 16, all of which are powers of 2.
 *
 * Example 2:
 *
 * Input: deliciousness = [1,1,1,3,3,3,7]
 * Output: 15
 * Explanation: The good meals are (1,1) with 3 ways, (1,3) with 9 ways, and (1,7) with 3 ways.
 *
 *
 * Constraints:
 *
 * 1 <= deliciousness.length <= 10^5
 * 0 <= deliciousness[i] <= 2^20
 *
 */
public class CountGoodMeals {

    private static final int MOD = 1_000_000_007;
    // values are <= 2^20, so a pair sums to at most 2^21: the targets are 2^0 .. 2^21
    private static final int POWERS_OF_TWO = 22;

    // V0
    // IDEA: HASH TABLE + ENUMERATE THE 22 POSSIBLE POWER-OF-TWO SUMS ("two sum" per target)
    //       count only partners seen earlier, so each unordered pair is counted exactly once
    /**
     * time = O(N * 22)
     * space = O(N)
     */
    public int countPairs(int[] deliciousness) {
        // value -> how many earlier items had it
        Map<Integer, Integer> seenCount = new HashMap<>();
        long meals = 0;

        for (int value : deliciousness) {
            int target = 1;
            for (int p = 0; p < POWERS_OF_TWO; p++) {
                meals += seenCount.getOrDefault(target - value, 0);
                target *= 2;
            }
            seenCount.put(value, seenCount.getOrDefault(value, 0) + 1);
        }
        return (int) (meals % MOD);
    }
}
