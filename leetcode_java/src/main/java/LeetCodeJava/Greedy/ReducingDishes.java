package LeetCodeJava.Greedy;

// https://leetcode.com/problems/reducing-dishes/description/

import java.util.*;

/**
 * 1402. Reducing Dishes
 * Hard
 *
 * A chef has collected data on the satisfaction level of his n dishes.
 * Chef can cook any dish in 1 unit of time.
 *
 * Like-time coefficient of a dish is defined as the time taken to cook that dish
 * including previous dishes multiplied by its satisfaction level
 * i.e. time[i] * satisfaction[i].
 *
 * Return the maximum sum of like-time coefficient that the chef can obtain after
 * preparing some amount of dishes.
 *
 * Dishes can be prepared in any order and the chef can discard some dishes to get
 * this maximum value.
 *
 *
 * Example 1:
 *
 * Input: satisfaction = [-1,-8,0,5,-9]
 * Output: 14
 * Explanation: After Removing the second and last dish, the maximum total
 * like-time coefficient will be equal to (-1*1 + 0*2 + 5*3 = 14).
 * Each dish is prepared in one unit of time.
 *
 * Example 2:
 *
 * Input: satisfaction = [4,3,2]
 * Output: 20
 * Explanation: Dishes can be prepared in any order, (2*1 + 3*2 + 4*3 = 20)
 *
 * Example 3:
 *
 * Input: satisfaction = [-1,-4,-5]
 * Output: 0
 * Explanation: People do not like the dishes. No dish is prepared.
 *
 *
 * Constraints:
 *
 * n == satisfaction.length
 * 1 <= n <= 500
 * -1000 <= satisfaction[i] <= 1000
 *
 */
public class ReducingDishes {

    // V0
    // IDEA: GREEDY + SORT (add dishes from best to worst while the running suffix sum stays positive)
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxSatisfaction(int[] satisfaction) {
        int[] sorted = satisfaction.clone();
        Arrays.sort(sorted);

        int best = 0;
        // suffixSum = sum of the dishes chosen so far
        int suffixSum = 0;

        // walk from the most satisfying dish down. putting one more (smaller) dish
        // in front shifts every chosen dish one slot later, which adds suffixSum
        for (int i = sorted.length - 1; i >= 0; i--) {
            suffixSum += sorted[i];
            if (suffixSum <= 0) {
                // from here on every extra dish only lowers the total
                break;
            }
            best += suffixSum;
        }
        return best;
    }

    // V1
    // IDEA: SORT + TRY EVERY CUT (keep the suffix sorted[i:], cook it in ascending order)
    /**
     * time = O(N^2)
     * space = O(N)
     */
    public int maxSatisfaction_1(int[] satisfaction) {
        int[] sorted = satisfaction.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        int best = 0;
        for (int start = 0; start < n; start++) {
            // dish sorted[start] is cooked at time 1, the next at time 2, ...
            int total = 0;
            int time = 1;
            for (int j = start; j < n; j++) {
                total += time * sorted[j];
                time++;
            }
            best = Math.max(best, total);
        }
        return best;
    }
}
