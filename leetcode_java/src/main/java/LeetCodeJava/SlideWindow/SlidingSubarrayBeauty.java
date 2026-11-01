package LeetCodeJava.SlideWindow;

// https://leetcode.com/problems/sliding-subarray-beauty/description/

/**
 * 2653. Sliding Subarray Beauty
 * Medium
 *
 * Given an integer array nums containing n integers, find the beauty of each subarray of size k.
 *
 * The beauty of a subarray is the xth smallest integer in the subarray if it is negative, or 0 if
 * there are fewer than x negative integers.
 *
 * Return an integer array containing n - k + 1 integers, which denote the beauty of the subarrays
 * in order from the first index in the array.
 *
 * A subarray is a contiguous non-empty sequence of elements within an array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,-1,-3,-2,3], k = 3, x = 2
 * Output: [-1,-2,-2]
 * Explanation: There are 3 subarrays with size k = 3.
 * The first subarray is [1, -1, -3] and the 2nd smallest negative integer is -1.
 * The second subarray is [-1, -3, -2] and the 2nd smallest negative integer is -2.
 * The third subarray is [-3, -2, 3] and the 2nd smallest negative integer is -2.
 *
 * Example 2:
 *
 * Input: nums = [-1,-2,-3,-4,-5], k = 2, x = 2
 * Output: [-1,-2,-3,-4]
 *
 * Example 3:
 *
 * Input: nums = [-3,1,2,-3,0,-3], k = 2, x = 1
 * Output: [-3,0,-3,-3,-3]
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^5
 * 1 <= k <= n
 * 1 <= x <= k
 * -50 <= nums[i] <= 50
 *
 */
public class SlidingSubarrayBeauty {

    // values are in [-50, 50]; negative v is stored at slot v + OFFSET
    private static final int OFFSET = 50;

    // V0
    // IDEA: FIXED-SIZE SLIDING WINDOW + COUNTING SORT ON THE NEGATIVE VALUES
    //  values live in [-50, 50] and only negatives can be the answer, so a 50-slot
    //  frequency table replaces a heap / ordered multiset; each window walks it once.
    /**
     * time = O(N * 50)
     * space = O(1)
     */
    public int[] getSubarrayBeauty(int[] nums, int k, int x) {
        int n = nums.length;
        int[] result = new int[n - k + 1];

        // count[v + 50] = how many times negative value v is in the window
        int[] count = new int[OFFSET];

        for (int i = 0; i < n; i++) {
            // nums[i] enters the window (zeros / positives are never the answer)
            if (nums[i] < 0) {
                count[nums[i] + OFFSET]++;
            }
            // nums[i - k] leaves the window
            if (i >= k) {
                int leaving = nums[i - k];
                if (leaving < 0) {
                    count[leaving + OFFSET]--;
                }
            }
            // a full window ends at i: find its x-th smallest negative
            if (i >= k - 1) {
                int seen = 0;
                int beauty = 0; // fewer than x negatives -> 0
                for (int slot = 0; slot < OFFSET; slot++) {
                    seen += count[slot];
                    if (seen >= x) {
                        beauty = slot - OFFSET;
                        break;
                    }
                }
                result[i - k + 1] = beauty;
            }
        }
        return result;
    }
}
