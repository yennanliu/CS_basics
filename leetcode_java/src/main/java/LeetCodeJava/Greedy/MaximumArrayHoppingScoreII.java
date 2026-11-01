package LeetCodeJava.Greedy;

// https://leetcode.com/problems/maximum-array-hopping-score-ii/description/

import java.util.*;

/**
 * 3221. Maximum Array Hopping Score II
 * Medium
 *
 * 🔒 (premium)
 *
 * Given an array nums, you have to get the maximum score starting from index 0 and hopping until you reach the last element of the array.
 *
 * In each hop, you can jump from index i to an index j > i, and you get a score of (j - i) * nums[j].
 *
 * Return the maximum score you can get.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,5,8]
 * Output: 16
 * Explanation:
 * There are two possible ways to reach the last element:
 * 0 -> 1 -> 2 with a score of (1 - 0) * 5 + (2 - 1) * 8 = 13.
 * 0 -> 2 with a score of (2 - 0) * 8 = 16.
 *
 * Example 2:
 *
 * Input: nums = [4,5,2,8,9,1,3]
 * Output: 42
 * Explanation:
 * We can do the hopping 0 -> 4 -> 6 with a score of (4 - 0) * 9 + (6 - 4) * 3 = 42.
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^5
 *
 */
public class MaximumArrayHoppingScoreII {

    // V0
    // IDEA: ALWAYS HOP TO THE LARGEST VALUE STILL AHEAD - THE SUFFIX MAXIMA
    /**
     *  A hop i -> j scores (j - i) * nums[j], so every step of distance is paid
     *  at the value of the index we land on. The best landing value for each
     *  step is the maximum of everything ahead, so hop through the strict
     *  suffix maxima (scanning from the right) in left-to-right order.
     *
     *  time = O(N)
     *  space = O(N)
     */
    public long maxScore(int[] nums) {
        int n = nums.length;

        // suffix maxima, collected right to left (index 0 is the start, never a target)
        List<Integer> maxima = new ArrayList<>();
        int currentMax = 0;
        for (int i = n - 1; i >= 1; i--) {
            if (nums[i] > currentMax) {
                maxima.add(i);
                currentMax = nums[i];
            }
        }
        Collections.reverse(maxima);

        long total = 0;
        int previous = 0;
        for (int index : maxima) {
            total += (long) (index - previous) * nums[index];
            previous = index;
        }
        return total;
    }
}
