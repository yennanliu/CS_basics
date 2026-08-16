package LeetCodeJava.BackTrack;

// https://leetcode.com/problems/24-game/description/

import java.util.HashSet;
import java.util.Set;
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


    // V1
    // IDEA: UNORDERED PAIRS + an explicit 6-operation list
    /**
     *  V0 enumerates ORDERED pairs (i, j) so that a-b / b-a and a/b / b/a both
     *  appear. Picking i < j once and listing all SIX results explicitly halves
     *  the pair loop and makes the operator set visible at a glance.
     *
     *  time  = O(1) (bounded search)
     *  space = O(1)
     */
    public boolean judgePoint24_1(int[] cards) {
        List<Double> nums = new ArrayList<>();
        for (int c : cards) {
            nums.add((double) c);
        }
        return dfsPairs(nums);
    }

    private boolean dfsPairs(List<Double> nums) {
        int size = nums.size();
        if (size == 1) {
            return Math.abs(nums.get(0) - 24.0) < 1e-6;
        }

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                double a = nums.get(i);
                double b = nums.get(j);

                List<Double> rest = new ArrayList<>();
                for (int t = 0; t < size; t++) {
                    if (t != i && t != j) {
                        rest.add(nums.get(t));
                    }
                }

                List<Double> results = new ArrayList<>();
                results.add(a + b);
                results.add(a * b);
                results.add(a - b);
                results.add(b - a);
                if (Math.abs(b) > 1e-6) {
                    results.add(a / b);
                }
                if (Math.abs(a) > 1e-6) {
                    results.add(b / a);
                }

                for (double v : results) {
                    rest.add(v);
                    if (dfsPairs(rest)) {
                        return true;
                    }
                    rest.remove(rest.size() - 1);
                }
            }
        }
        return false;
    }

    // V2
    // IDEA: ENUMERATE PERMUTATIONS x PARENTHESIZATIONS EXPLICITLY
    /**
     *  With exactly 4 numbers there are only 4! orders and 5 binary-tree shapes:
     *
     *      ((a?b)?c)?d    (a?(b?c))?d    (a?b)?(c?d)
     *      a?((b?c)?d)    a?(b?(c?d))
     *
     *  Enumerating those directly mirrors how a person would attack the puzzle,
     *  and it can PRINT the winning expression, which the folding versions cannot
     *  without extra bookkeeping.
     *
     *  time  = O(1) (4! * 5 * 4^3 evaluations)
     *  space = O(1)
     */
    public boolean judgePoint24_2(int[] cards) {
        int[] idx = { 0, 1, 2, 3 };
        return permute24(cards, idx, 0);
    }

    private boolean permute24(int[] cards, int[] idx, int pos) {
        if (pos == 4) {
            double a = cards[idx[0]];
            double b = cards[idx[1]];
            double c = cards[idx[2]];
            double d = cards[idx[3]];
            for (double ab : apply(a, b)) {
                for (double abc : apply(ab, c)) {
                    for (double v : apply(abc, d)) {          // ((a?b)?c)?d
                        if (Math.abs(v - 24) < 1e-6) {
                            return true;
                        }
                    }
                }
                for (double cd : apply(c, d)) {
                    for (double v : apply(ab, cd)) {          // (a?b)?(c?d)
                        if (Math.abs(v - 24) < 1e-6) {
                            return true;
                        }
                    }
                }
            }
            for (double bc : apply(b, c)) {
                for (double abc : apply(a, bc)) {
                    for (double v : apply(abc, d)) {          // (a?(b?c))?d
                        if (Math.abs(v - 24) < 1e-6) {
                            return true;
                        }
                    }
                }
                for (double bcd : apply(bc, d)) {
                    for (double v : apply(a, bcd)) {          // a?((b?c)?d)
                        if (Math.abs(v - 24) < 1e-6) {
                            return true;
                        }
                    }
                }
            }
            for (double cd : apply(c, d)) {
                for (double bcd : apply(b, cd)) {
                    for (double v : apply(a, bcd)) {          // a?(b?(c?d))
                        if (Math.abs(v - 24) < 1e-6) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        for (int i = pos; i < 4; i++) {
            swap(idx, pos, i);
            if (permute24(cards, idx, pos + 1)) {
                return true;
            }
            swap(idx, pos, i);
        }
        return false;
    }

    /** every value reachable from the ordered pair (x, y) */
    private double[] apply(double x, double y) {
        if (Math.abs(y) > 1e-6) {
            return new double[] { x + y, x - y, x * y, x / y };
        }
        return new double[] { x + y, x - y, x * y };
    }

    private void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    // V3
    // IDEA: BITMASK DP over SUBSETS (set of reachable values per subset)
    /**
     *  reach[mask] = every value obtainable from exactly the cards in `mask`.
     *
     *  Build it bottom up: split each mask into two non-empty halves and combine
     *  every value of one with every value of the other.
     *
     *  Unlike the DFS versions this COMPUTES ALL reachable totals, so the same
     *  table answers `can we make 24?` and `what totals are possible?` at once,
     *  and it generalises to n cards without deepening the recursion.
     *
     *  time  = O(3^n * V^2), n = 4
     *  space = O(2^n * V)
     */
    public boolean judgePoint24_3(int[] cards) {
        int n = cards.length;
        List<Set<Double>> reach = new ArrayList<>();
        for (int i = 0; i < (1 << n); i++) {
            reach.add(new HashSet<>());
        }
        for (int i = 0; i < n; i++) {
            reach.get(1 << i).add((double) cards[i]);
        }

        for (int mask = 1; mask < (1 << n); mask++) {
            if (Integer.bitCount(mask) < 2) {
                continue;
            }
            // split mask into `sub` and its complement inside mask
            for (int sub = (mask - 1) & mask; sub > 0; sub = (sub - 1) & mask) {
                int other = mask ^ sub;
                if (sub > other) {
                    continue; // each unordered split once
                }
                for (double x : reach.get(sub)) {
                    for (double y : reach.get(other)) {
                        for (double v : apply(x, y)) {
                            reach.get(mask).add(v);
                        }
                        for (double v : apply(y, x)) {
                            reach.get(mask).add(v);
                        }
                    }
                }
            }
        }

        for (double v : reach.get((1 << n) - 1)) {
            if (Math.abs(v - 24.0) < 1e-6) {
                return true;
            }
        }
        return false;
    }

}
