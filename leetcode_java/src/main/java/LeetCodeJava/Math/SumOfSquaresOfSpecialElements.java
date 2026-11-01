package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-squares-of-special-elements/description/

/**
 * 2778. Sum of Squares of Special Elements
 * Easy
 *
 * You are given a 1-indexed integer array nums of length n.
 *
 * An element nums[i] of nums is called special if i divides n, i.e. n % i == 0.
 *
 * Return the sum of the squares of all special elements of nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4]
 * Output: 21
 *
 * Example 2:
 *
 * Input: nums = [2,7,1,19,18,3]
 * Output: 63
 *
 *
 * Constraints:
 *
 * 1 <= nums.length == n <= 50
 * 1 <= nums[i] <= 50
 *
 */
public class SumOfSquaresOfSpecialElements {

    // V0
    // IDEA: DIRECT SCAN -- "special" depends on the 1-based POSITION dividing n
    /**
     * time = O(N)
     * space = O(1)
     */
    public int sumOfSquares(int[] nums) {
        int n = nums.length;
        int total = 0;
        for (int j = 0; j < n; j++) {
            // the statement is 1-indexed: array offset j is position j + 1
            int position = j + 1;
            if (n % position == 0) {
                total += nums[j] * nums[j];
            }
        }
        return total;
    }
}
