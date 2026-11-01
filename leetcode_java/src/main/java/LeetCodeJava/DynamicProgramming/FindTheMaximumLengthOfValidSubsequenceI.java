package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-length-of-valid-subsequence-i/description/

/**
 * 3201. Find the Maximum Length of Valid Subsequence I
 * Medium
 *
 * You are given an integer array nums.
 *
 * A subsequence sub of nums with length x is called valid if it satisfies:
 *
 * (sub[0] + sub[1]) % 2 == (sub[1] + sub[2]) % 2 == ... == (sub[x - 2] + sub[x - 1]) % 2.
 *
 * Return the length of the longest valid subsequence of nums.
 *
 * Note: A subsequence is an array that can be derived from another array by deleting some or no elements without changing the order of the remaining elements.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 4
 * Explanation:
 * The longest valid subsequence is [1, 2, 3, 4].
 *
 * Example 2:
 *
 * Input: nums = [1,2,1,1,2,1,2]
 * Output: 6
 * Explanation:
 * The longest valid subsequence is [1, 2, 1, 2, 1, 2].
 *
 * Example 3:
 *
 * Input: nums = [1,3]
 * Output: 2
 * Explanation:
 * The longest valid subsequence is [1, 3].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 2 * 10^5
 * 1 <= nums[i] <= 10^7
 *
 */
public class FindTheMaximumLengthOfValidSubsequenceI {

    // V0
    // IDEA: ONLY THREE SHAPES - ALL EVEN, ALL ODD, OR STRICTLY ALTERNATING PARITY
    //   All adjacent sums even -> every element has the same parity: take all evens or all odds.
    //   All adjacent sums odd  -> parity flips every step: greedily take each element whose
    //   parity differs from the last one taken (taking the first possible one never hurts).
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumLength(int[] nums) {
        int evenCount = 0;
        int oddCount = 0;
        int alternatingLength = 0;
        // parity the alternating pick wants next; -1 = no element taken yet
        int wantedParity = -1;

        for (int x : nums) {
            int parity = x % 2;
            if (parity == 0) {
                evenCount++;
            } else {
                oddCount++;
            }
            if (wantedParity == -1 || parity == wantedParity) {
                alternatingLength++;
                wantedParity = 1 - parity;
            }
        }
        return Math.max(alternatingLength, Math.max(evenCount, oddCount));
    }
}
