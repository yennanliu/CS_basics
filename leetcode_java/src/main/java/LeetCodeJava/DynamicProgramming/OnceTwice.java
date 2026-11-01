package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/once-twice/description/

import java.util.*;

/**
 * 3595. Once Twice
 * Medium
 *
 * You are given an integer array nums. In this array:
 *
 * Exactly one element appears once.
 * Exactly one element appears twice.
 * All other elements appear exactly three times.
 *
 * Return an integer array of length 2, where the first element is the one that
 * appears once, and the second is the one that appears twice.
 *
 * Your solution must run in O(n) time and O(1) space.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,2,3,2,5,5,5,7,7]
 * Output: [3,7]
 * Explanation:
 * The element 3 appears once, and the element 7 appears twice. The remaining
 * elements each appear three times.
 *
 * Example 2:
 *
 * Input: nums = [4,4,6,4,9,9,9,6,8]
 * Output: [8,6]
 * Explanation:
 * The element 8 appears once, and the element 6 appears twice. The remaining
 * elements each appear three times.
 *
 *
 * Constraints:
 *
 * 3 <= nums.length <= 10^5
 * -2^31 <= nums[i] <= 2^31 - 1
 * nums.length is a multiple of 3
 * Exactly one element appears once, one element appears twice, and all other
 * elements appear three times.
 *
 */
public class OnceTwice {

    // used as splitBit to count over the whole array
    private static final int NO_FILTER = -1;

    // V0
    // IDEA: COUNT EACH BIT MOD 3, THEN SPLIT ON A BIT WHERE THE TWO ANSWERS DIFFER
    //   let a = the once element, b = the twice element. Triples add 0 mod 3, so the
    //   count of bit i over the whole array is (a_i + 2 * b_i) mod 3:
    //     1 -> only a has it, 2 -> only b has it, 0 -> a and b agree on it.
    //   a != b, so some bit has a non-zero residue. Splitting the array on that bit
    //   puts a and b in different halves and keeps every triple together; inside
    //   a's half the residues are a's bits, inside b's half residue 2 marks b's bits.
    //   only 32 counters are used -> O(1) extra space.
    /**
     * time = O(N)
     * space = O(1)
     */
    public int[] onceTwice(int[] nums) {
        int[] residue = bitCountsMod3(nums, NO_FILTER, false);

        // a bit where a and b differ
        int splitBit = 0;
        for (int bit = 0; bit < 32; bit++) {
            if (residue[bit] != 0) {
                splitBit = bit;
                break;
            }
        }
        // residue 1 at splitBit -> the once element has that bit set
        boolean onceHasBit = residue[splitBit] == 1;

        int[] onceSide = bitCountsMod3(nums, splitBit, onceHasBit);
        int[] twiceSide = bitCountsMod3(nums, splitBit, !onceHasBit);

        int once = 0;
        int twice = 0;
        for (int bit = 0; bit < 32; bit++) {
            if (onceSide[bit] == 1) {
                once |= (1 << bit);
            }
            if (twiceSide[bit] == 2) {
                twice |= (1 << bit);
            }
        }
        return new int[] {once, twice};
    }

    /** Per bit, the count of set bits mod 3 over the elements whose splitBit is set == wantSet (all elements for NO_FILTER). */
    private int[] bitCountsMod3(int[] nums, int splitBit, boolean wantSet) {
        int[] counts = new int[32];
        for (int num : nums) {
            if (splitBit != NO_FILTER) {
                boolean hasBit = ((num >> splitBit) & 1) == 1;
                if (hasBit != wantSet) {
                    continue;
                }
            }
            for (int bit = 0; bit < 32; bit++) {
                // >> keeps the sign, but & 1 reads just this one bit
                if (((num >> bit) & 1) == 1) {
                    counts[bit] = (counts[bit] + 1) % 3;
                }
            }
        }
        return counts;
    }
}
