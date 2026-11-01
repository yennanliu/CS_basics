package LeetCodeJava.Stack;

// https://leetcode.com/problems/find-the-most-competitive-subsequence/description/

/**
 * 1673. Find the Most Competitive Subsequence
 * Medium
 *
 * Given an integer array nums and a positive integer k, return the most competitive
 * subsequence of nums of size k.
 *
 * An array's subsequence is a resulting sequence obtained by erasing some (possibly
 * zero) elements from the array.
 *
 * We define that a subsequence a is more competitive than a subsequence b (of the
 * same length) if in the first position where a and b differ, subsequence a has a
 * number less than the corresponding number in b. For example, [1,3,4] is more
 * competitive than [1,3,5] because the first position they differ is at the final
 * number, and 4 is less than 5.
 *
 *
 * Example 1:
 *
 * Input: nums = [3,5,2,6], k = 2
 * Output: [2,6]
 *
 * Example 2:
 *
 * Input: nums = [2,4,3,3,5,4,9,6], k = 4
 * Output: [2,3,3,4]
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 10^9
 * 1 <= k <= nums.length
 *
 */
public class FindTheMostCompetitiveSubsequence {

    // V0
    // IDEA: MONOTONIC INCREASING STACK + FEASIBILITY GUARD
    //       pop a bigger top when a smaller value arrives (makes an earlier slot
    //       smaller), but only while enough elements remain to still fill k slots:
    //       stackSize + (n - i) > k. Push only while the stack holds fewer than k.
    /**
     * time = O(N)
     * space = O(K)
     */
    public int[] mostCompetitive(int[] nums, int k) {
        int n = nums.length;
        // an array used as a stack; size is the number of live entries
        int[] stack = new int[k];
        int size = 0;

        for (int i = 0; i < n; i++) {
            int remaining = n - i; // including nums[i]
            while (size > 0 && stack[size - 1] > nums[i] && size + remaining > k) {
                size--;
            }
            if (size < k) {
                stack[size] = nums[i];
                size++;
            }
        }
        return stack;
    }
}
