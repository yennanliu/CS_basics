package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-sequence-value-of-array/description/

/**
 * 3287. Find the Maximum Sequence Value of Array
 * Hard
 *
 * You are given an integer array nums and a positive integer k.
 *
 * The value of a sequence seq of size 2 * x is defined as:
 *
 * (seq[0] OR seq[1] OR ... OR seq[x - 1]) XOR (seq[x] OR seq[x + 1] OR ... OR seq[2 * x - 1]).
 *
 * Return the maximum value of any subsequence of nums having size 2 * k.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,6,7], k = 1
 * Output: 5
 * Explanation:
 * The subsequence [2, 7] has the maximum value of 2 XOR 7 = 5.
 *
 * Example 2:
 *
 * Input: nums = [4,2,5,6,7], k = 2
 * Output: 2
 * Explanation:
 * The subsequence [4, 5, 6, 7] has the maximum value of (4 OR 5) XOR (6 OR 7) = 2.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 400
 * 1 <= nums[i] < 2^7
 * 1 <= k <= nums.length / 2
 *
 */
public class FindTheMaximumSequenceValueOfArray {

    /** nums[i] < 2^7, so any OR of them is one of 128 values. */
    private static final int VALUE_LIMIT = 1 << 7;

    // V0
    // IDEA: PREFIX / SUFFIX "REACHABLE OR" DP + TRY EVERY SPLIT POINT
    /**
     *  Once the split point is fixed the two halves are independent:
     *
     *     answer = max over cuts of max { a XOR b }
     *        a = an OR of k elements taken LEFT of the cut
     *        b = an OR of k elements taken RIGHT of the cut
     *
     *  DP def (prefix side; the suffix side is symmetric):
     *     can[j][x] = true if some j elements of the prefix seen so far OR to x
     *
     *  DP eq (adding element v, j from high to low so v is used once):
     *     can[j][x | v] |= can[j - 1][x]
     *
     *  Init: can[0][0] = true (the OR of nothing is 0)
     *
     *  leftSets[i][x]  = x reachable with k elements from nums[0 .. i-1]
     *  rightSets[i][x] = x reachable with k elements from nums[i .. n-1]
     *  Answer: max over cuts i, x in leftSets[i], y in rightSets[i] of x ^ y
     *
     *  time = O(N * K * 128 + N * 128^2)
     *  space = O(N * 128 + K * 128)
     */
    public int maxValue(int[] nums, int k) {
        int n = nums.length;

        boolean[][] leftSets = buildReachableSets(nums, k, true);
        boolean[][] rightSets = buildReachableSets(nums, k, false);

        int best = 0;
        // the left part needs k elements, the right part needs k elements
        for (int cut = k; cut <= n - k; cut++) {
            for (int leftOr = 0; leftOr < VALUE_LIMIT; leftOr++) {
                if (!leftSets[cut][leftOr]) {
                    continue;
                }
                for (int rightOr = 0; rightOr < VALUE_LIMIT; rightOr++) {
                    if (rightSets[cut][rightOr]) {
                        best = Math.max(best, leftOr ^ rightOr);
                    }
                }
            }
        }
        return best;
    }

    /**
     * fromLeft: result[i][x] = x reachable with k elements of nums[0 .. i-1].
     * otherwise: result[i][x] = x reachable with k elements of nums[i .. n-1].
     */
    private boolean[][] buildReachableSets(int[] nums, int k, boolean fromLeft) {
        int n = nums.length;
        boolean[][] result = new boolean[n + 1][VALUE_LIMIT];

        // can[j][x]: OR value x reachable using exactly j of the elements seen so far
        boolean[][] can = new boolean[k + 1][VALUE_LIMIT];
        can[0][0] = true;

        // the empty prefix / suffix boundary
        int boundary = fromLeft ? 0 : n;
        result[boundary] = can[k].clone();

        for (int step = 0; step < n; step++) {
            int index = fromLeft ? step : n - 1 - step;
            int value = nums[index];

            // j goes downward so this element is counted at most once
            for (int j = k; j >= 1; j--) {
                for (int x = 0; x < VALUE_LIMIT; x++) {
                    if (can[j - 1][x]) {
                        can[j][x | value] = true;
                    }
                }
            }

            // prefix after index covers [0, index]; suffix from index covers [index, n)
            int position = fromLeft ? index + 1 : index;
            result[position] = can[k].clone();
        }
        return result;
    }
}
