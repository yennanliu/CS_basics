package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/partition-array-for-maximum-xor-and-and/description/

import java.util.*;

/**
 * 3630. Partition Array for Maximum XOR and AND
 * Hard
 *
 * You are given an integer array nums.
 *
 * Partition the array into three (possibly empty) subsequences A, B, and C
 * such that every element of nums belongs to exactly one subsequence.
 *
 * Your goal is to maximize the value of: XOR(A) + AND(B) + XOR(C)
 *
 * where:
 *
 * XOR(arr) denotes the bitwise XOR of all elements in arr. If arr is empty,
 * its value is defined as 0.
 * AND(arr) denotes the bitwise AND of all elements in arr. If arr is empty,
 * its value is defined as 0.
 *
 * Return the maximum value achievable.
 *
 * Note: If multiple partitions result in the same maximum sum, you can
 * consider any one of them.
 *
 *
 * Example 1:
 *
 * Input: nums = [2,3]
 * Output: 5
 * Explanation:
 * One optimal partition is:
 * A = [3], XOR(A) = 3
 * B = [2], AND(B) = 2
 * C = [], XOR(C) = 0
 * The maximum value of: XOR(A) + AND(B) + XOR(C) = 3 + 2 + 0 = 5. Thus, the
 * answer is 5.
 *
 * Example 2:
 *
 * Input: nums = [1,3,2]
 * Output: 6
 * Explanation:
 * One optimal partition is:
 * A = [1], XOR(A) = 1
 * B = [2], AND(B) = 2
 * C = [3], XOR(C) = 3
 * The maximum value of: XOR(A) + AND(B) + XOR(C) = 1 + 2 + 3 = 6. Thus, the
 * answer is 6.
 *
 * Example 3:
 *
 * Input: nums = [2,3,6,7]
 * Output: 15
 * Explanation:
 * One optimal partition is:
 * A = [7], XOR(A) = 7
 * B = [2,3], AND(B) = 2
 * C = [6], XOR(C) = 6
 * The maximum value of: XOR(A) + AND(B) + XOR(C) = 7 + 2 + 6 = 15. Thus, the
 * answer is 15.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 19
 * 1 <= nums[i] <= 10^9
 *
 */
public class PartitionArrayForMaximumXORAndAND {

    // V0
    // IDEA: ENUMERATE B (BITMASK), SOLVE THE A/C SPLIT WITH A XOR LINEAR BASIS
    //       n <= 19, so enumerate the subset B. let S = the rest, T = XOR(S).
    //       for x = XOR(A), XOR(C) = x ^ T and u + w = (u ^ w) + 2 * (u & w), so
    //         XOR(A) + XOR(C) = T + 2 * (x & ~T).
    //       the reachable x are the GF(2) span of S; masking is linear, so mask each
    //       element with ~T first, build a xor basis, and take the greedy max over it.
    //       answer = max over B of AND(B) + T + 2 * maxMaskedXor.
    //       (AND of the empty B is taken as 0.)
    /**
     * time = O(2^N * N * 32)
     * space = O(2^N)
     */
    public long maximizeXorAndXor(int[] nums) {
        int n = nums.length;
        int subsets = 1 << n;
        int fullMask = subsets - 1;

        // andOf[mask] / xorOf[mask] for every subset, built from the subset minus one element
        int[] andOf = new int[subsets];
        int[] xorOf = new int[subsets];
        for (int mask = 1; mask < subsets; mask++) {
            int lowest = Integer.numberOfTrailingZeros(mask); // index of the lowest chosen element
            int rest = mask & (mask - 1);                     // mask without that element
            andOf[mask] = (rest == 0) ? nums[lowest] : (andOf[rest] & nums[lowest]);
            xorOf[mask] = xorOf[rest] ^ nums[lowest];
        }

        long best = 0;
        int[] basis = new int[31]; // basis[b] = vector whose highest set bit is b, or 0
        for (int bMask = 0; bMask < subsets; bMask++) {
            int restXor = xorOf[fullMask ^ bMask];
            int keepBits = ~restXor;

            // xor basis of the remaining elements, each masked to the bits outside T
            Arrays.fill(basis, 0);
            for (int i = 0; i < n; i++) {
                if ((bMask & (1 << i)) != 0) {
                    continue; // element i belongs to B
                }
                insert(basis, nums[i] & keepBits);
            }

            // greedy maximum over the span, highest bit first
            int maxMasked = 0;
            for (int bit = 30; bit >= 0; bit--) {
                if ((maxMasked ^ basis[bit]) > maxMasked) {
                    maxMasked ^= basis[bit];
                }
            }

            long value = (long) andOf[bMask] + restXor + 2L * maxMasked;
            best = Math.max(best, value);
        }
        return best;
    }

    /** Gaussian elimination step: reduce x by the basis and store it under its top bit. */
    private void insert(int[] basis, int x) {
        for (int bit = 30; bit >= 0 && x != 0; bit--) {
            if ((x & (1 << bit)) == 0) {
                continue;
            }
            if (basis[bit] == 0) {
                basis[bit] = x;
                return;
            }
            x ^= basis[bit];
        }
    }
}
