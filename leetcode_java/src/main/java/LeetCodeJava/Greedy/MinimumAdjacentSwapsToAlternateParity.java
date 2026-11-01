package LeetCodeJava.Greedy;

// https://leetcode.com/problems/minimum-adjacent-swaps-to-alternate-parity/description/

import java.util.*;

/**
 * 3587. Minimum Adjacent Swaps to Alternate Parity
 * Medium
 *
 * You are given an array nums of distinct integers.
 *
 * In one operation, you can swap any two adjacent elements in the array.
 *
 * An arrangement of the array is considered valid if the parity of adjacent elements alternates, meaning every pair of neighboring elements consists of one even and one odd number.
 *
 * Return the minimum number of adjacent swaps required to transform nums into any valid arrangement.
 *
 * If it is impossible to rearrange nums such that no two adjacent elements have the same parity, return -1.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,4,6,5,7]
 * Output: 3
 * Explanation:
 * Swapping 5 and 6, the array becomes [2,4,5,6,7]
 * Swapping 5 and 4, the array becomes [2,5,4,6,7]
 * Swapping 6 and 7, the array becomes [2,5,4,7,6]. The array is now a valid
 * arrangement. Thus, the answer is 3.
 *
 * Example 2:
 *
 * Input: nums = [2,4,5,7]
 * Output: 1
 * Explanation:
 * By swapping 4 and 5, the array becomes [2,5,4,7], which is a valid
 * arrangement. Thus, the answer is 1.
 *
 * Example 3:
 *
 * Input: nums = [1,2,3]
 * Output: 0
 * Explanation:
 * The array is already a valid arrangement. Thus, no operations are needed.
 *
 * Example 4:
 *
 * Input: nums = [4,5,6,8]
 * Output: -1
 * Explanation:
 * No valid arrangement is possible. Thus, the answer is -1.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * All elements in nums are distinct.
 *
 */
public class MinimumAdjacentSwapsToAlternateParity {

    // V0
    // IDEA: GREEDY MATCHING — SWAPS = SUM OF |CURRENT INDEX - TARGET INDEX| OF ONE PARITY
    //       an optimal solution never reorders two numbers of the same parity, so the
    //       t-th odd (or even) number goes to slot 2t. the swap count equals the total
    //       displacement of that one class. valid only if the counts differ by <= 1,
    //       and the majority class must take the even slots (try both when equal).
    /**
     * time = O(N)
     * space = O(N)
     */
    public int minSwaps(int[] nums) {
        List<Integer> oddPositions = new ArrayList<>();
        List<Integer> evenPositions = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] % 2 != 0) {
                oddPositions.add(i);
            } else {
                evenPositions.add(i);
            }
        }
        if (Math.abs(oddPositions.size() - evenPositions.size()) > 1) {
            return -1;
        }

        long best = Long.MAX_VALUE;
        if (oddPositions.size() >= evenPositions.size()) {
            best = Math.min(best, displacement(oddPositions));
        }
        if (evenPositions.size() >= oddPositions.size()) {
            best = Math.min(best, displacement(evenPositions));
        }
        return (int) best;
    }

    /** Total distance to move the t-th position to slot 2t. */
    private long displacement(List<Integer> positions) {
        long moves = 0;
        for (int t = 0; t < positions.size(); t++) {
            moves += Math.abs(positions.get(t) - 2 * t);
        }
        return moves;
    }
}
