package LeetCodeJava.Array;

// https://leetcode.com/problems/max-consecutive-ones-ii/description/
/**
 * 487. Max Consecutive Ones II
 * Medium
 * Lock: Prime
 *
 * Given a binary array nums, return the maximum number of consecutive 1's in the array
 * if you can flip at most one 0.
 *
 * Example 1:
 *
 * Input: nums = [1,0,1,1,0]
 * Output: 4
 * Explanation:
 * - If we flip the first zero, nums becomes [1,1,1,1,0] and we have 4 consecutive ones.
 * - If we flip the second zero, nums becomes [1,0,1,1,1] and we have 3 consecutive ones.
 * The max number of consecutive ones is 4.
 *
 * Example 2:
 *
 * Input: nums = [1,0,1,1,0,1]
 * Output: 4
 * Explanation:
 * - If we flip the first zero, nums becomes [1,1,1,1,0,1] and we have 4 consecutive ones.
 * - If we flip the second zero, nums becomes [1,0,1,1,1,1] and we have 4 consecutive ones.
 * The max number of consecutive ones is 4.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 *
 * Follow up: What if the input numbers come in one by one as an infinite stream?
 * In other words, you can't store all numbers coming from the stream as it's too large
 * to hold in memory. Could you solve it efficiently?
 *
 */
public class MaxConsecutiveOnes2 {

    // V0
    // IDEA: SLIDING WINDOW (at most one 0 inside the window)
    /**
     *  time  = O(n)
     *  space = O(1)
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int left = 0;
        int zeroCnt = 0;
        int res = 0;

        for (int right = 0; right < nums.length; right++) {
            if (nums[right] == 0) {
                zeroCnt += 1;
            }

            /** NOTE !!!
             *
             *  shrink from left until the window holds AT MOST one 0
             */
            while (zeroCnt > 1) {
                if (nums[left] == 0) {
                    zeroCnt -= 1;
                }
                left += 1;
            }

            res = Math.max(res, right - left + 1);
        }

        return res;
    }

    // V0-1
    // IDEA: STREAMING (O(1) space, single pass, no random access on nums)
    /**
     *  -> keeps only `# of 1s before the last 0` and `# of 1s after the last 0`
     *  -> this is what answers the FOLLOW UP (infinite stream)
     *
     *  time  = O(n)
     *  space = O(1)
     */
    public int findMaxConsecutiveOnes_0_1(int[] nums) {
        // edge
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int prev = 0; // count of consecutive 1s BEFORE the most recent 0
        int cur = 0;  // count of consecutive 1s AFTER the most recent 0
        boolean seenZero = false;
        int res = 0;

        for (int x : nums) {
            if (x == 1) {
                cur += 1;
            } else {
                /** NOTE !!!
                 *
                 *  the current 0 becomes the (only) flipped one,
                 *  so the `after` streak becomes the new `before` streak
                 */
                prev = cur;
                cur = 0;
                seenZero = true;
            }
            // `+1` only if there is actually a 0 we can flip
            res = Math.max(res, prev + cur + (seenZero ? 1 : 0));
        }

        return res;
    }

}
