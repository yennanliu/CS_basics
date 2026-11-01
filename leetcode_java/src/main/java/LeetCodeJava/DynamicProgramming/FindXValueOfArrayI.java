package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-x-value-of-array-i/description/

/**
 * 3524. Find X Value of Array I
 * Medium
 *
 * You are given an array of positive integers nums, and a positive integer k.
 *
 * You are allowed to perform an operation once on nums, where in each operation you can
 * remove any non-overlapping prefix and suffix from nums such that nums remains non-empty.
 *
 * You need to find the x-value of nums, which is the number of ways to perform this
 * operation so that the product of the remaining elements leaves a remainder of x when
 * divided by k.
 *
 * Return an array result of size k where result[x] is the x-value of nums for
 * 0 <= x <= k - 1.
 *
 * Note that the prefix and suffix to be chosen for the operation can be empty.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], k = 3
 * Output: [9,2,4]
 *
 * Example 2:
 *
 * Input: nums = [1,2,4,8,16,32], k = 4
 * Output: [18,1,2,0]
 *
 * Example 3:
 *
 * Input: nums = [1,1,2,1,1], k = 2
 * Output: [9,6]
 *
 *
 * Constraints:
 *
 * 1 <= nums[i] <= 10^9
 * 1 <= nums.length <= 10^5
 * 1 <= k <= 5
 *
 */
public class FindXValueOfArrayI {

    // V0
    // IDEA: DP -- ROLLING RESIDUE HISTOGRAM OF ALL SUBARRAYS ENDING AT THE CURRENT INDEX
    /**
     *  Removing a prefix and a suffix (keeping something) == picking one non-empty subarray,
     *  so count subarrays by the residue of their product mod k.
     *
     *  DP def:
     *     - endingHere[r] = number of subarrays ending at j whose product % k == r
     *
     *  DP eq:
     *     - each subarray ending at j-1 extended by nums[j]:  next[r * v % k] += endingHere[r]
     *     - plus the single-element subarray:                 next[v] += 1
     *     (v = nums[j] % k; the residue of p * v depends only on p % k)
     *
     *  Answer: result[r] = sum over j of endingHere[r]
     *
     *  time  = O(N * K)
     *  space = O(K)
     */
    public long[] resultArray(int[] nums, int k) {
        long[] result = new long[k];
        long[] endingHere = new long[k];

        for (int num : nums) {
            int value = num % k;
            long[] next = new long[k];
            // extend every subarray that ended at the previous index
            for (int r = 0; r < k; r++) {
                if (endingHere[r] > 0) {
                    next[(r * value) % k] += endingHere[r];
                }
            }
            // the subarray holding only this element
            next[value] += 1;
            endingHere = next;

            for (int r = 0; r < k; r++) {
                result[r] += endingHere[r];
            }
        }
        return result;
    }
}
