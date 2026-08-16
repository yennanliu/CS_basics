package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/24-game/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 679. 24 Game
 * Hard
 *
 * You are given an integer array cards of length 4. You have four cards, each containing a
 * number in the range [1, 9]. You should arrange the numbers on these cards in a
 * mathematical expression using the operators ['+', '-', '*', '/'] and the parentheses
 * '(' and ')' to get the value 24.
 *
 * You are restricted with the following rules:
 *
 * - The division operator '/' represents real division, not integer division.
 *   For example, 4 / (1 - 2 / 3) = 4 / (1 / 3) = 12.
 * - Every operation done is between two numbers. In particular, we cannot use '-' as a
 *   unary operator.
 *   For example, if cards = [1, 1, 1, 1], the expression "-1 - 1 - 1 - 1" is not allowed.
 * - You cannot concatenate numbers together.
 *   For example, if cards = [1, 2, 1, 2], the expression "12 + 12" is not valid.
 *
 * Return true if you can get such expression that evaluates to 24, and false otherwise.
 *
 * Example 1:
 *
 * Input: cards = [4,1,8,7]
 * Output: true
 * Explanation: (8-4) * (7-1) = 24
 *
 * Example 2:
 *
 * Input: cards = [1,2,1,2]
 * Output: false
 *
 * Constraints:
 *
 * cards.length == 4
 * 1 <= cards[i] <= 9
 *
 */
public class TwentyFourGame {

    // V0
    // IDEA: DFS / BACKTRACKING -- repeatedly `fold` two numbers into one
    /**
     *   Any fully parenthesized expression over 4 numbers can be built by picking
     *   TWO of the current numbers, replacing them with the result of one operator,
     *   and recursing on the shrunken multiset (4 -> 3 -> 2 -> 1).
     *   This covers EVERY parenthesization automatically, so we never build strings.
     *
     *   ORDERED pairs (i, j) with i != j are enumerated, which covers both
     *   `a - b` / `b - a` and `a / b` / `b / a` without a separate case.
     *
     *   Division is REAL division, so we work in doubles and compare against 24 with
     *   an epsilon (e.g. 8/(3-8/3) is exactly 24 mathematically but NOT in binary float).
     *
     *   time  = O(1) -- bounded search: at most 12 * 4 * 6 * 4 * 2 * 4 states for 4 cards
     *   space = O(1)
     */

    private static final double EPS = 1e-6;

    public boolean judgePoint24(int[] cards) {
        List<Double> nums = new ArrayList<>();
        for (int c : cards) {
            nums.add((double) c);
        }
        return dfs(nums);
    }

    private boolean dfs(List<Double> nums) {
        if (nums.size() == 1) {
            /** NOTE !!!
             *
             *  compare with EPS, NOT with `== 24`,
             *  since real division introduces float error
             */
            return Math.abs(nums.get(0) - 24.0) < EPS;
        }

        int size = nums.size();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == j) {
                    continue;
                }

                // everything EXCEPT the two chosen operands
                List<Double> rest = new ArrayList<>();
                for (int t = 0; t < size; t++) {
                    if (t != i && t != j) {
                        rest.add(nums.get(t));
                    }
                }

                double a = nums.get(i);
                double b = nums.get(j);

                List<Double> candidates = new ArrayList<>();
                candidates.add(a + b);
                candidates.add(a - b);
                candidates.add(a * b);
                if (Math.abs(b) > EPS) {
                    candidates.add(a / b);
                }

                for (double val : candidates) {
                    rest.add(val);
                    if (dfs(rest)) {
                        return true;
                    }
                    // backtrack : drop the folded value we just appended
                    rest.remove(rest.size() - 1);
                }
            }
        }

        return false;
    }

}
