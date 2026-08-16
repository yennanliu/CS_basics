package LeetCodeJava.Array;

// https://leetcode.com/problems/max-consecutive-ones/description/
/**
 * 485. Max Consecutive Ones
 * Easy
 *
 * Given a binary array nums, return the maximum number of consecutive 1's in the
 * array.
 *
 * Example 1:
 *
 * Input: nums = [1,1,0,1,1,1]
 * Output: 3
 * Explanation: The first two digits or the last three digits are consecutive 1s.
 * The maximum number of consecutive 1s is 3.
 *
 * Example 2:
 *
 * Input: nums = [1,0,1,1,0,1]
 * Output: 2
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class MaxConsecutiveOnes {

    // V0
    // IDEA: ONE PASS RUNNING COUNTER
    /**
     *  Keep a counter of the current streak of 1's; a 0 RESETS it to 0.
     *  Track the best streak seen so far.
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int best = 0;
        int cur = 0;

        for (int x : nums) {
            if (x == 1) {
                cur += 1;
                best = Math.max(best, cur);
            } else {
                cur = 0; // streak broken
            }
        }

        return best;
    }

}
