package LeetCodeJava.TwoPointer;

// https://leetcode.com/problems/minimum-swaps-to-group-all-1s-together-ii/description/

/**
 * 2134. Minimum Swaps to Group All 1's Together II
 * Medium
 *
 * A swap is defined as taking two distinct positions in an array and swapping the values in them.
 *
 * A circular array is defined as an array where we consider the first element and the last element to be adjacent.
 *
 * Given a binary circular array nums, return the minimum number of swaps required to group all 1's present in the array together at any location.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,0,1,1,0,0]
 * Output: 1
 * Explanation: Here are a few of the ways to group all the 1's together:
 * [0,0,1,1,1,0,0] using 1 swap.
 * [0,1,1,1,0,0,0] using 1 swap.
 * [1,1,0,0,0,0,1] using 2 swaps (using the circular property of the array).
 * There is no way to group all 1's together with 0 swaps.
 * Thus, the minimum number of swaps required is 1.
 *
 * Example 2:
 *
 * Input: nums = [0,1,1,1,0,0,1,1,0]
 * Output: 2
 * Explanation: Here are a few of the ways to group all the 1's together:
 * [1,1,1,0,0,0,0,1,1] using 2 swaps (using the circular property of the array).
 * [1,1,1,1,1,0,0,0,0] using 2 swaps.
 * There is no way to group all the 1's together with 0 or 1 swaps.
 * Thus, the minimum number of swaps required is 2.
 *
 * Example 3:
 *
 * Input: nums = [1,1,0,0,1]
 * Output: 0
 * Explanation: All the 1's are already grouped together due to the circular property of the array.
 * Thus, the minimum number of swaps required is 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * nums[i] is either 0 or 1.
 *
 */
public class MinimumSwapsToGroupAll1STogetherII {

    // V0
    // IDEA: FIXED-WIDTH CIRCULAR SLIDING WINDOW — MINIMISE THE ZEROS INSIDE
    //  in the end all `ones` 1s sit in one circular block of width `ones`. each 0 currently in
    //  that block costs exactly one swap, so the answer is the minimum number of zeros over all
    //  circular windows of width `ones`. index modulo n to wrap around.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int minSwaps(int[] nums) {
        int n = nums.length;
        int ones = 0;
        for (int x : nums) {
            ones += x;
        }
        // nothing to group, or nothing to swap with
        if (ones == 0 || ones == n) {
            return 0;
        }

        // zeros in the first window nums[0 .. ones - 1]
        int zeros = 0;
        for (int i = 0; i < ones; i++) {
            if (nums[i] == 0) {
                zeros++;
            }
        }

        int best = zeros;
        for (int start = 1; start < n; start++) {
            // window slides from [start - 1, start + ones - 2] to [start, start + ones - 1]
            if (nums[start - 1] == 0) {
                zeros--;
            }
            if (nums[(start + ones - 1) % n] == 0) {
                zeros++;
            }
            best = Math.min(best, zeros);
        }
        return best;
    }
}
