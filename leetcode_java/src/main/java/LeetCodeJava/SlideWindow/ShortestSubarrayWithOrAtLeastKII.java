package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/shortest-subarray-with-or-at-least-k-ii/

/**
 *  3097. Shortest Subarray With OR at Least K II
 *  Medium
 *
 *  You are given an array nums of non-negative integers and an integer k.
 *
 *  An array is called special if the bitwise OR of all of its elements is at
 *  least k.
 *
 *  Return the length of the shortest special non-empty subarray of nums, or
 *  return -1 if no special subarray exists.
 *
 *  Example 1:
 *    Input: nums = [1,2,3], k = 2
 *    Output: 1
 *    Explanation: The subarray [3] has OR value of 3.
 *
 *  Example 2:
 *    Input: nums = [2,1,8], k = 10
 *    Output: 3
 *    Explanation: The subarray [2,1,8] has OR value of 11.
 *
 *  Example 3:
 *    Input: nums = [1,2], k = 0
 *    Output: 1
 *
 *  Constraints:
 *    1 <= nums.length <= 2 * 10^5
 *    0 <= nums[i] <= 10^9
 *    0 <= k <= 10^9
 */
public class ShortestSubarrayWithOrAtLeastKII {

    private static final int BITS = 31;

    // V0
    // IDEA: SLIDING WINDOW WITH A COUNTER PER BIT (OR IS NOT INVERTIBLE)
    //       the OR of a window only grows as it widens, so the window is
    //       monotone and two pointers apply: extend right until the OR reaches
    //       k, then shrink from the left while it still does.
    //       the catch is that OR cannot be "undone" when an element leaves —
    //       losing a 1 bit only matters if no OTHER element in the window
    //       still has it. so keep a count of how many window elements set each
    //       bit; a bit is present in the OR exactly while its count is
    //       non-zero, and rebuilding the OR is 31 cheap steps per move.
    /**
     * time = O(31 * N)
     * space = O(1)
     */
    public int minimumSubarrayLength(int[] nums, int k) {
        int[] cnt = new int[BITS];
        int best = Integer.MAX_VALUE;
        int left = 0;

        for (int right = 0; right < nums.length; right++) {
            add(cnt, nums[right], 1);
            int cur = value(cnt);
            while (left <= right && cur >= k) {
                best = Math.min(best, right - left + 1);
                add(cnt, nums[left], -1);
                left++;
                cur = value(cnt);
            }
        }
        return (best == Integer.MAX_VALUE) ? -1 : best;
    }

    private void add(int[] cnt, int x, int delta) {
        for (int b = 0; b < BITS; b++) {
            if (((x >> b) & 1) == 1) {
                cnt[b] += delta;
            }
        }
    }

    private int value(int[] cnt) {
        int v = 0;
        for (int b = 0; b < BITS; b++) {
            if (cnt[b] > 0) {
                v |= (1 << b);
            }
        }
        return v;
    }
}
