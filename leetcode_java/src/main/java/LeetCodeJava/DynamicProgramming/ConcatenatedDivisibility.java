package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/concatenated-divisibility/description/

import java.util.*;

/**
 * 3533. Concatenated Divisibility
 * Hard
 *
 * You are given an array of positive integers nums and a positive integer k.
 *
 * A permutation of nums is said to form a divisible concatenation if, when you
 * concatenate the decimal representations of the numbers in the order specified by
 * the permutation, the resulting number is divisible by k.
 *
 * Return the lexicographically smallest permutation (when considered as a list of
 * integers) that forms a divisible concatenation. If no such permutation exists,
 * return an empty list.
 *
 * Example 1:
 *
 * Input: nums = [3,12,45], k = 5
 *
 * Output: [3,12,45]
 *
 * Explanation:
 *
 * Permutation  | Concatenated Value | Divisible by 5
 * -------------+--------------------+---------------
 * [3, 12, 45]  | 31245              | Yes
 * [3, 45, 12]  | 34512              | No
 * [12, 3, 45]  | 12345              | Yes
 * [12, 45, 3]  | 12453              | No
 * [45, 3, 12]  | 45312              | No
 * [45, 12, 3]  | 45123              | No
 *
 * The lexicographically smallest permutation that forms a divisible concatenation
 * is [3,12,45].
 *
 * Example 2:
 *
 * Input: nums = [10,5], k = 10
 *
 * Output: [5,10]
 *
 * Explanation:
 *
 * Permutation | Concatenated Value | Divisible by 10
 * ------------+--------------------+----------------
 * [5, 10]     | 510                | Yes
 * [10, 5]     | 105                | No
 *
 * The lexicographically smallest permutation that forms a divisible concatenation
 * is [5,10].
 *
 * Example 3:
 *
 * Input: nums = [1,2,3], k = 5
 *
 * Output: []
 *
 * Explanation:
 *
 * Since no permutation of nums forms a valid divisible concatenation, return an
 * empty list.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 13
 *
 * 1 <= nums[i] <= 10^5
 *
 * 1 <= k <= 100
 *
 */
public class ConcatenatedDivisibility {

    // V0
    // IDEA: BITMASK DP OF REACHABLE REMAINDERS PER SUBSET, THEN GREEDY RECONSTRUCTION
    //   putting x in FRONT of an already-built number Q with L digits gives
    //   x * 10^L + Q, so the residue of a subset's concatenation depends on the
    //   subset (which fixes its digit length) and on the order inside it.
    //   reachable[mask][r] = some ordering of the numbers in mask concatenates to r (mod k)
    //   reachable[mask][(r + nums[i] * 10^len(mask - i)) % k] |= reachable[mask - i][r]
    //   Then build the lexicographically smallest answer greedily: try the unused
    //   numbers in increasing order and take the first one whose remainder can
    //   still be completed to 0 by the numbers left over.
    /**
     * time = O(2^N * N * K)
     * space = O(2^N * K)
     */
    public int[] concatenatedDivisibility(int[] nums, int k) {
        int n = nums.length;
        int totalStates = 1 << n;
        int fullMask = totalStates - 1;

        // digit count and residue of each number
        int[] digitCount = new int[n];
        int[] residue = new int[n];
        int maxDigits = 0;
        for (int i = 0; i < n; i++) {
            digitCount[i] = String.valueOf(nums[i]).length();
            residue[i] = nums[i] % k;
            maxDigits += digitCount[i];
        }

        // powerOfTen[t] = 10^t mod k
        int[] powerOfTen = new int[maxDigits + 1];
        powerOfTen[0] = 1 % k;
        for (int t = 1; t <= maxDigits; t++) {
            powerOfTen[t] = powerOfTen[t - 1] * 10 % k;
        }

        // totalDigits[mask] = digit length of the concatenation of mask's numbers
        int[] totalDigits = new int[totalStates];
        for (int mask = 1; mask < totalStates; mask++) {
            int lowest = Integer.numberOfTrailingZeros(mask);
            totalDigits[mask] = totalDigits[mask & ~(1 << lowest)] + digitCount[lowest];
        }

        boolean[][] reachable = new boolean[totalStates][k];
        // the empty concatenation is the number 0
        reachable[0][0] = true;
        for (int mask = 1; mask < totalStates; mask++) {
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) == 0) {
                    continue;
                }
                // nums[i] goes first, in front of some ordering of the rest
                int rest = mask & ~(1 << i);
                int shift = residue[i] * powerOfTen[totalDigits[rest]] % k;
                for (int r = 0; r < k; r++) {
                    if (reachable[rest][r]) {
                        reachable[mask][(r + shift) % k] = true;
                    }
                }
            }
        }

        // no ordering of all the numbers is divisible by k
        if (!reachable[fullMask][0]) {
            return new int[0];
        }

        // indices sorted by value, so the greedy tries the smallest number first
        Integer[] order = new Integer[n];
        for (int i = 0; i < n; i++) {
            order[i] = i;
        }
        Arrays.sort(order, (a, b) -> Integer.compare(nums[a], nums[b]));

        int[] result = new int[n];
        int usedMask = 0;
        int prefixResidue = 0;
        for (int slot = 0; slot < n; slot++) {
            for (int index : order) {
                int bit = 1 << index;
                if ((usedMask & bit) != 0) {
                    continue;
                }
                int newPrefix = (prefixResidue * powerOfTen[digitCount[index]] + residue[index]) % k;
                int rest = fullMask & ~usedMask & ~bit;
                // whole number = newPrefix * 10^len(rest) + Q, need it to be 0 mod k
                int needed = (k - newPrefix * powerOfTen[totalDigits[rest]] % k) % k;
                if (reachable[rest][needed]) {
                    usedMask |= bit;
                    prefixResidue = newPrefix;
                    result[slot] = nums[index];
                    break;
                }
            }
        }
        return result;
    }
}
