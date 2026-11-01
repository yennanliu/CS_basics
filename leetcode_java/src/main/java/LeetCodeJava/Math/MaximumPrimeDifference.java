package LeetCodeJava.Math;

// https://leetcode.com/problems/maximum-prime-difference/description/

/**
 * 3115. Maximum Prime Difference
 * Medium
 *
 * You are given an integer array nums.
 *
 * Return an integer that is the maximum distance between the indices of two (not necessarily different)
 * prime numbers in nums.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,2,9,5,3]
 * Output: 3
 * Explanation: nums[1], nums[3], and nums[4] are prime. So the answer is |4 - 1| = 3.
 *
 * Example 2:
 *
 * Input: nums = [4,8,2,8]
 * Output: 0
 * Explanation: nums[2] is prime. Because there is just one prime number, the answer is |2 - 2| = 0.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 3 * 10^5
 * 1 <= nums[i] <= 100
 * The input is generated such that the number of prime numbers in the nums is at least one.
 *
 */
public class MaximumPrimeDifference {

    private static final int MAX_VALUE = 100;

    // V0
    // IDEA: SIEVE UP TO 100 + FIRST / LAST PRIME INDEX
    //   the widest gap is always between the earliest and the latest prime.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int maximumPrimeDifference(int[] nums) {
        // sieve of Eratosthenes over the value range
        boolean[] isComposite = new boolean[MAX_VALUE + 1];
        isComposite[0] = true;
        isComposite[1] = true;
        for (int p = 2; p * p <= MAX_VALUE; p++) {
            if (!isComposite[p]) {
                for (int multiple = p * p; multiple <= MAX_VALUE; multiple += p) {
                    isComposite[multiple] = true;
                }
            }
        }

        int first = -1;
        int last = -1;
        for (int i = 0; i < nums.length; i++) {
            if (!isComposite[nums[i]]) {
                if (first == -1) {
                    first = i;
                }
                last = i;
            }
        }
        // a single prime gives first == last, i.e. 0
        return last - first;
    }
}
