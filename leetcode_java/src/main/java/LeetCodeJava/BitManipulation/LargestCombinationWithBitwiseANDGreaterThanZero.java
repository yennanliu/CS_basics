package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/largest-combination-with-bitwise-and-greater-than-zero/

/**
 *  2275. Largest Combination With Bitwise AND Greater Than Zero
 *  Medium
 *
 *  The bitwise AND of an array nums is the bitwise AND of all integers in nums.
 *  For example, for nums = [1, 5, 3], the bitwise AND is 1 & 5 & 3 = 1.
 *  Also, for nums = [7], the bitwise AND is 7.
 *
 *  You are given an array of positive integers candidates. Evaluate the bitwise
 *  AND of every combination of numbers of candidates. Each number in candidates
 *  may only be used once in each combination.
 *
 *  Return the size of the largest combination of candidates with a bitwise AND
 *  greater than 0.
 *
 *  Example 1:
 *    Input: candidates = [16,17,71,62,12,24,14]
 *    Output: 4
 *    Explanation: [16,17,62,24] has AND 16 & 17 & 62 & 24 = 16 > 0, and no
 *                 combination of size > 4 has a positive AND.
 *
 *  Example 2:
 *    Input: candidates = [8,8]
 *    Output: 2
 *
 *  Constraints:
 *    1 <= candidates.length <= 10^5
 *    1 <= candidates[i] <= 10^7
 */
public class LargestCombinationWithBitwiseANDGreaterThanZero {

    // V0
    // IDEA: THE AND IS NON-ZERO IFF SOME BIT SURVIVES -> COUNT PER BIT COLUMN
    //       a combination's AND is > 0 exactly when at least ONE bit position is
    //       set in every member, so for a fixed bit b the biggest such combination
    //       is simply "all candidates having bit b set". therefore
    //           answer = max over b of #{ x : x has bit b set }
    //       candidates are < 2^24, so 24 columns suffice.
    /**
     * time = O(N * 24)
     * space = O(24)
     */
    public int largestCombination(int[] candidates) {
        final int BITS = 24;               // 10^7 < 2^24
        int[] counts = new int[BITS];
        for (int x : candidates) {
            for (int b = 0; b < BITS; b++) {
                if (((x >> b) & 1) == 1) {
                    counts[b]++;
                }
            }
        }
        int res = 0;
        for (int c : counts) {
            res = Math.max(res, c);
        }
        return res;
    }
}
