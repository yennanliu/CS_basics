package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-the-maximum-length-of-valid-subsequence-ii/description/

/**
 * 3202. Find the Maximum Length of Valid Subsequence II
 * Medium
 *
 * You are given an integer array nums and a positive integer k.
 *
 * A subsequence sub of nums with length x is called valid if it satisfies:
 *
 * (sub[0] + sub[1]) % k == (sub[1] + sub[2]) % k == ... == (sub[x - 2] + sub[x - 1]) % k.
 *
 * Return the length of the longest valid subsequence of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], k = 2
 * Output: 5
 * Explanation:
 * The longest valid subsequence is [1, 2, 3, 4, 5].
 *
 * Example 2:
 *
 * Input: nums = [1,4,2,3,1,4], k = 3
 * Output: 4
 * Explanation:
 * The longest valid subsequence is [1, 4, 1, 4].
 *
 *
 * Constraints:
 *
 * 2 <= nums.length <= 10^3
 * 1 <= nums[i] <= 10^7
 * 1 <= k <= 10^3
 *
 */
public class FindTheMaximumLengthOfValidSubsequenceII {

    // V0
    // IDEA: FIX THE COMMON PAIR-SUM RESIDUE r, THEN DP ON THE LAST RESIDUE
    //   If every adjacent pair sums to r (mod k), an element with residue a must be followed
    //   by residue (r - a) mod k, so the subsequence alternates between two residue classes.
    //   For fixed r: best[a] = longest valid subsequence ending in residue a
    //   best[a] = max(best[a], best[(r - a) mod k] + 1) as each element is scanned.
    //   Try all k values of r.
    /**
     * time = O(N * K)
     * space = O(K)
     */
    public int maximumLength(int[] nums, int k) {
        int longest = 0;
        for (int r = 0; r < k; r++) {
            int[] best = new int[k];
            for (int x : nums) {
                int residue = x % k;
                // the residue the previous element must have so the pair sums to r
                int partner = ((r - residue) % k + k) % k;
                best[residue] = Math.max(best[residue], best[partner] + 1);
                longest = Math.max(longest, best[residue]);
            }
        }
        return longest;
    }
}
