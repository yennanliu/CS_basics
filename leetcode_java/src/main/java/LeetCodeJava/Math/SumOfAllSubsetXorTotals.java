package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-all-subset-xor-totals/description/

/**
 * 1863. Sum of All Subset XOR Totals
 * Easy
 *
 * The XOR total of an array is defined as the bitwise XOR of all its elements, or 0 if the array is empty.
 *
 * For example, the XOR total of the array [2,5,6] is 2 XOR 5 XOR 6 = 1.
 *
 * Given an array nums, return the sum of all XOR totals for every subset of nums.
 *
 * Note: Subsets with the same elements should be counted multiple times.
 *
 * An array a is a subset of an array b if a can be obtained from b by deleting some (possibly zero) elements of b.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,3]
 * Output: 6
 * Explanation: The 4 subsets of [1,3] are:
 * - The empty subset has an XOR total of 0.
 * - [1] has an XOR total of 1.
 * - [3] has an XOR total of 3.
 * - [1,3] has an XOR total of 1 XOR 3 = 2.
 * 0 + 1 + 3 + 2 = 6
 *
 * Example 2:
 *
 * Input: nums = [5,1,6]
 * Output: 28
 * Explanation: The 8 subsets of [5,1,6] are:
 * - The empty subset has an XOR total of 0.
 * - [5] has an XOR total of 5.
 * - [1] has an XOR total of 1.
 * - [6] has an XOR total of 6.
 * - [5,1] has an XOR total of 5 XOR 1 = 4.
 * - [5,6] has an XOR total of 5 XOR 6 = 3.
 * - [1,6] has an XOR total of 1 XOR 6 = 7.
 * - [5,1,6] has an XOR total of 5 XOR 1 XOR 6 = 2.
 * 0 + 5 + 1 + 6 + 4 + 3 + 7 + 2 = 28
 *
 * Example 3:
 *
 * Input: nums = [3,4,5,6,7,8]
 * Output: 480
 * Explanation: The sum of all XOR totals for every subset is 480.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 12
 * 1 <= nums[i] <= 20
 *
 */
public class SumOfAllSubsetXorTotals {

    // V0
    // IDEA: BIT CONTRIBUTION -> answer = OR(nums) * 2^(n-1)
    //  a bit owned by at least one element is set in exactly half of the 2^n subsets
    //  (toggle one owner of that bit to pair subsets up).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int subsetXORSum(int[] nums) {
        int bitsPresent = 0;
        for (int num : nums) {
            bitsPresent |= num;
        }
        // each present bit contributes its value in 2^(n-1) subsets; << (n-1) multiplies by 2^(n-1)
        return bitsPresent << (nums.length - 1);
    }

    // V1
    // IDEA: BACKTRACKING OVER SUBSETS (take / skip each element; n <= 12 -> 4096 subsets)
    //  the standard enumeration; use it when the bit argument does not come to mind.
    /**
     * time = O(2^N)
     * space = O(N) recursion depth
     */
    public int subsetXORSum_1(int[] nums) {
        return dfs(nums, 0, 0);
    }

    /** Sum of XOR totals of all subsets of nums[index..], given the XOR of what was taken so far. */
    private int dfs(int[] nums, int index, int currentXor) {
        if (index == nums.length) {
            return currentXor;
        }
        int withElement = dfs(nums, index + 1, currentXor ^ nums[index]);
        int withoutElement = dfs(nums, index + 1, currentXor);
        return withElement + withoutElement;
    }
}
