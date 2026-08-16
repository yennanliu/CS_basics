package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/tallest-billboard/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 956. Tallest Billboard
 * Hard
 *
 * You are installing a billboard and want it to have the largest height. The billboard
 * will have two steel supports, one on each side. Each steel support must be an equal
 * height.
 *
 * You are given a collection of rods that can be welded together. For example, if you
 * have rods of lengths 1, 2, and 3, you can weld them together to make a support of
 * length 6.
 *
 * Return the largest possible height of your billboard installation. If you cannot
 * support the billboard, return 0.
 *
 * Example 1:
 *
 * Input: rods = [1,2,3,6]
 * Output: 6
 * Explanation: We have two disjoint subsets {1,2,3} and {6}, which have the same sum = 6.
 *
 * Example 2:
 *
 * Input: rods = [1,2,3,4,5,6]
 * Output: 10
 * Explanation: We have two disjoint subsets {2,3,5} and {4,6}, which have the same
 * sum = 10.
 *
 * Example 3:
 *
 * Input: rods = [1,2]
 * Output: 0
 * Explanation: The billboard cannot be supported, so we return 0.
 *
 * Constraints:
 *
 * 1 <= rods.length <= 20
 * 1 <= rods[i] <= 1000
 * sum(rods[i]) <= 5000
 *
 */
public class TallestBillboard {

    // V0
    // IDEA: DP on the DIFFERENCE between the two supports
    /**
     *  DP def:
     *     - dp[d] = the MAXIMUM height of the TALLER support, given that
     *               (taller - shorter) == d
     *     - the shorter support is then simply (dp[d] - d)
     *     - dp[0] IS the answer: both supports equal, height = dp[0]
     *
     *  NOTE !!! keying on the DIFFERENCE (not on the two heights) is what collapses
     *           the state space from O(S^2) down to O(S).
     *
     *  For each rod h, from state (d, taller) we may:
     *     1) DROP the rod             -> state unchanged
     *     2) weld it on the TALLER side  -> diff = d + h,  taller = taller + h
     *     3) weld it on the SHORTER side -> new shorter = (taller - d) + h,
     *                                       new diff / taller recomputed via max/abs
     *                                       (the shorter side may OVERTAKE the taller)
     *
     *  NOTE !!! we iterate over a SNAPSHOT of dp so every rod is used AT MOST ONCE
     *           (0/1 knapsack semantics).
     *
     *  time  = O(n * S), n = rods.length, S = sum(rods)
     *  space = O(S)
     */
    public int tallestBillboard(int[] rods) {
        // dp[diff] = max height of the taller support
        Map<Integer, Integer> dp = new HashMap<>();
        dp.put(0, 0);

        for (int h : rods) {
            Map<Integer, Integer> snapshot = new HashMap<>(dp);

            for (Map.Entry<Integer, Integer> e : snapshot.entrySet()) {
                int d = e.getKey();
                int taller = e.getValue();
                int shorter = taller - d;

                // case 2 : add the rod to the TALLER side
                if (dp.getOrDefault(d + h, -1) < taller + h) {
                    dp.put(d + h, taller + h);
                }

                // case 3 : add the rod to the SHORTER side (it may overtake)
                int newShorter = shorter + h;
                int newDiff = Math.abs(newShorter - taller);
                int newTaller = Math.max(newShorter, taller);
                if (dp.getOrDefault(newDiff, -1) < newTaller) {
                    dp.put(newDiff, newTaller);
                }
            }
        }

        return dp.get(0);
    }

}
