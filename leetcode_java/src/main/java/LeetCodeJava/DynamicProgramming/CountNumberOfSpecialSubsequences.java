package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/count-number-of-special-subsequences/description/
/**
 * 1955. Count Number of Special Subsequences
 * Hard
 *
 * A sequence is special if it consists of a positive number of 0s, followed by a positive
 * number of 1s, then a positive number of 2s.
 *
 * For example, [0,1,2] and [0,0,1,1,1,2] are special.
 * In contrast, [2,1,0], [1], and [0,1,2,0] are not special.
 *
 * Given an array nums (consisting of only integers 0, 1, and 2), return the number of
 * different subsequences that are special. Since the answer may be very large, return it
 * modulo 10^9 + 7.
 *
 * A subsequence of an array is a sequence that can be derived from the array by deleting
 * some or no elements without changing the order of the remaining elements. Two
 * subsequences are different if the set of indices chosen are different.
 *
 *
 * Example 1:
 *
 * Input: nums = [0,1,2,2]
 * Output: 3
 * Explanation: The special subsequences are bolded [0,1,2,2], [0,1,2,2], and [0,1,2,2].
 *
 * Example 2:
 *
 * Input: nums = [2,2,0,0]
 * Output: 0
 * Explanation: There are no special subsequences in [2,2,0,0].
 *
 * Example 3:
 *
 * Input: nums = [0,1,2,0,1,2]
 * Output: 7
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 0 <= nums[i] <= 2
 *
 */
public class CountNumberOfSpecialSubsequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP ON "WHICH BLOCK ARE WE IN" (3 ROLLING COUNTERS, ONE PASS)
    /**
     *  DP def:
     *     - endsInZeros = number of subsequences 0...0           (only 0s so far)
     *     - endsInOnes  = number of subsequences 0...0 1...1
     *     - endsInTwos  = number of complete special subsequences 0...0 1...1 2...2
     *
     *  DP eq:
     *     - see a 0: endsInZeros = 2 * endsInZeros + 1       (skip / extend, or start new)
     *     - see a 1: endsInOnes  = endsInZeros + 2 * endsInOnes  (promote / skip / extend)
     *     - see a 2: endsInTwos  = endsInOnes  + 2 * endsInTwos
     *
     *  The "2 *" is the take-or-skip factor: subsequences with different index sets
     *  are counted separately, exactly as the problem requires.
     *
     *  time = O(N)
     *  space = O(1)
     */
    public int countSpecialSubsequences(int[] nums) {
        long endsInZeros = 0;
        long endsInOnes = 0;
        long endsInTwos = 0;

        for (int num : nums) {
            if (num == 0) {
                // each existing 0-prefix may skip or take this 0, plus the 0 alone
                endsInZeros = (2 * endsInZeros + 1) % MOD;
            } else if (num == 1) {
                // a 0-prefix can be promoted by this 1; a 1-prefix may skip or take it
                endsInOnes = (endsInZeros + 2 * endsInOnes) % MOD;
            } else {
                // a 1-prefix can be completed by this 2; a full one may skip or take it
                endsInTwos = (endsInOnes + 2 * endsInTwos) % MOD;
            }
        }
        return (int) endsInTwos;
    }
}
