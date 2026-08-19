package LeetCodeJava.Stack;

// https://leetcode.com/problems/minimum-adjacent-swaps-for-k-consecutive-ones/

/**
 *  1703. Minimum Adjacent Swaps for K Consecutive Ones
 *  Hard
 *
 *  You are given an integer array nums (only 0's and 1's) and an integer k.
 *  In one move, you can choose two adjacent indices and swap their values.
 *
 *  Return the minimum number of moves required so that nums has k consecutive 1's.
 *
 *  Example 1:
 *  Input: nums = [1,0,0,1,0,1], k = 2
 *  Output: 1
 *
 *  Example 2:
 *  Input: nums = [1,0,0,0,0,0,1,1], k = 3
 *  Output: 5
 *
 *  Example 3:
 *  Input: nums = [1,1,0,1], k = 2
 *  Output: 0
 *
 *  Constraints:
 *  1 <= nums.length <= 10^5
 *  nums[i] is 0 or 1.
 *  1 <= k <= sum(nums)
 */
public class MinimumAdjacentSwapsForKConsecutiveOnes {

    /**
     *  IDEA:
     *
     *  Let p[0..m-1] be the positions of the 1's. Gathering the k ones of a window
     *  p[i..i+k-1] into consecutive slots costs
     *
     *      sum |p[j] - j - x|   (x = target start offset)
     *
     *  which is minimised at the MEDIAN of the window. Working directly on p (without
     *  subtracting j) the cost becomes
     *
     *      (sum of the right half) - (sum of the left half) - (k/2) * ((k+1)/2)
     *
     *  where the last term removes the "already consecutive" offsets. A prefix-sum array
     *  over p turns each window into O(1) work.
     */

    // V0
    // IDEA: PREFIX SUM over the positions of the 1's + fixed-size sliding window around the median
    /**
     * time = O(n)
     * space = O(n)
     */
    public int minMoves(int[] nums, int k) {

        // positions of the 1's
        int m = 0;
        for (int v : nums) {
            if (v == 1) {
                m++;
            }
        }
        int[] p = new int[m];
        int idx = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] == 1) {
                p[idx++] = i;
            }
        }

        if (k <= 1 || m < k) {
            return 0;
        }

        // prefix[i] = p[0] + ... + p[i-1]
        long[] prefix = new long[m + 1];
        for (int i = 0; i < m; i++) {
            prefix[i + 1] = prefix[i] + p[i];
        }

        int left = k / 2;          // size of the left half (median exclusive when k is odd)
        int right = (k + 1) / 2;   // size of the right half

        long best = Long.MAX_VALUE;
        for (int i = 0; i + k <= m; i++) {
            /**
             *  window = p[i .. i+k-1]
             *  right half sum  = prefix[i+k]     - prefix[i+right]
             *  left  half sum  = prefix[i+left]  - prefix[i]
             *  (the middle element, if k is odd, cancels out)
             */
            long cost = (prefix[i + k] - prefix[i + right]) - (prefix[i + left] - prefix[i]);
            best = Math.min(best, cost);
        }

        // remove the cost of the gaps that already exist between consecutive target slots
        best -= (long) left * right;
        return (int) best;
    }
}
