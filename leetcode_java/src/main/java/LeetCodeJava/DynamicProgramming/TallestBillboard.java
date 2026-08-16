package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/tallest-billboard/description/

import java.util.Arrays;
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


    // V1
    // IDEA: ARRAY-INDEXED DP over the difference (offset by the total sum)
    /**
     *  The difference ranges over [-S, S], so an int array of size 2S+1 replaces
     *  the HashMap -- no boxing, no hashing, and the iteration order is
     *  deterministic.
     *
     *  time  = O(n * S)
     *  space = O(S)
     */
    public int tallestBillboard_1(int[] rods) {
        int total = 0;
        for (int r : rods) {
            total += r;
        }
        final int NEG = Integer.MIN_VALUE / 2;

        // dp[d] = tallest TALLER support with (taller - shorter) == d
        int[] dp = new int[total + 1];
        Arrays.fill(dp, NEG);
        dp[0] = 0;

        for (int r : rods) {
            int[] ndp = dp.clone();
            for (int d = 0; d <= total; d++) {
                if (dp[d] == NEG) {
                    continue;
                }
                int taller = dp[d];
                int shorter = taller - d;

                // add to the taller side
                if (d + r <= total) {
                    ndp[d + r] = Math.max(ndp[d + r], taller + r);
                }
                // add to the shorter side (it may overtake)
                int ns = shorter + r;
                int nd = Math.abs(ns - taller);
                int nt = Math.max(ns, taller);
                if (nd <= total) {
                    ndp[nd] = Math.max(ndp[nd], nt);
                }
            }
            dp = ndp;
        }
        return dp[0];
    }

    // V2
    // IDEA: MEET IN THE MIDDLE over the 3-way assignment
    /**
     *  Each rod goes to the LEFT support, the RIGHT support, or is DROPPED -- 3^n
     *  assignments. Splitting the rods in half gives 3^(n/2) per side, and the two
     *  halves are joined on the difference key.
     *
     *  3^10 = 59049 per side at n = 20, so this stays fast even when the sum S is
     *  large -- exactly where the O(n * S) DP would struggle.
     *
     *  time  = O(3^(n/2))
     *  space = O(3^(n/2))
     */
    public int tallestBillboard_2(int[] rods) {
        int n = rods.length;
        int half = n / 2;

        Map<Integer, Integer> left = enumerate(rods, 0, half);
        Map<Integer, Integer> right = enumerate(rods, half, n);

        int best = 0;
        for (Map.Entry<Integer, Integer> e : left.entrySet()) {
            // a left difference of d must be cancelled by a right difference of -d
            Integer other = right.get(-e.getKey());
            if (other != null) {
                best = Math.max(best, e.getValue() + other);
            }
        }
        return best;
    }

    /** difference -> max height placed on the LEFT support, over rods[from, to) */
    private Map<Integer, Integer> enumerate(int[] rods, int from, int to) {
        Map<Integer, Integer> best = new HashMap<>();
        best.put(0, 0);
        for (int i = from; i < to; i++) {
            Map<Integer, Integer> next = new HashMap<>(best);
            for (Map.Entry<Integer, Integer> e : best.entrySet()) {
                int d = e.getKey();
                int leftH = e.getValue();
                // rod on the LEFT
                next.merge(d + rods[i], leftH + rods[i], Math::max);
                // rod on the RIGHT
                next.merge(d - rods[i], leftH, Math::max);
            }
            best = next;
        }
        return best;
    }

    // V3
    // IDEA: BRUTE FORCE over the 3^n assignments (tiny n)
    /**
     *  Try every rod in every role and keep the tallest balanced pair.
     *
     *  Only runs for n <= ~13, but it is the definition of the problem -- the
     *  oracle for the difference DP and the meet-in-the-middle join.
     *
     *  time  = O(3^n)
     *  space = O(n)
     */
    public int tallestBillboard_3(int[] rods) {
        return assign(rods, 0, 0, 0);
    }

    private int assign(int[] rods, int i, int leftH, int rightH) {
        if (i == rods.length) {
            return leftH == rightH ? leftH : -1;
        }
        int best = assign(rods, i + 1, leftH, rightH);                 // drop it
        best = Math.max(best, assign(rods, i + 1, leftH + rods[i], rightH));
        best = Math.max(best, assign(rods, i + 1, leftH, rightH + rods[i]));
        return best;
    }

}
