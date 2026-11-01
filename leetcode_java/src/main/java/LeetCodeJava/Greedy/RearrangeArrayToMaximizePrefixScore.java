package LeetCodeJava.Greedy;

// https://leetcode.com/problems/rearrange-array-to-maximize-prefix-score/description/

import java.util.*;

/**
 * 2587. Rearrange Array to Maximize Prefix Score
 * Medium
 *
 * You are given a 0-indexed integer array nums. You can rearrange the elements of nums to any order (including the given order).
 *
 * Let prefix be the array containing the prefix sums of nums after rearranging it. In other words, prefix[i] is the sum of the elements from 0 to i in nums after rearranging it. The score of nums is the number of positive integers in the array prefix.
 *
 * Return the maximum score you can achieve.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,-1,0,1,-3,3,-3]
 * Output: 6
 * Explanation: We can rearrange the array into nums = [2,3,1,-1,-3,0,-3].
 * prefix = [2,5,6,5,2,2,-1], so the score is 6.
 * It can be shown that 6 is the maximum score we can obtain.
 *
 * Example 2:
 *
 * Input: nums = [-2,-3,0]
 * Output: 0
 * Explanation: Any rearrangement of the array will result in a score of 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * -10^6 <= nums[i] <= 10^6
 *
 */
public class RearrangeArrayToMaximizePrefixScore {

    // V0
    // IDEA: GREEDY + SORT DESCENDING
    //       placing the largest numbers first maximises every prefix sum, and after that the
    //       prefix sums only go down -> count until the running sum stops being positive.
    /**
     * time = O(N log N)
     * space = O(N)
     */
    public int maxScore(int[] nums) {
        int n = nums.length;
        int[] sorted = Arrays.copyOf(nums, n);
        Arrays.sort(sorted);

        // long: 1e5 values of 1e6 exceed int
        long prefix = 0;
        int score = 0;
        // walk from the largest value down
        for (int i = n - 1; i >= 0; i--) {
            prefix += sorted[i];
            // "positive" means > 0, so a prefix of exactly 0 ends the count
            if (prefix <= 0) {
                break;
            }
            score++;
        }
        return score;
    }
}
