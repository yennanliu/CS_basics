package LeetCodeJava.BitManipulation;

// https://leetcode.com/problems/number-of-excellent-pairs/description/

import java.util.HashSet;
import java.util.Set;

/**
 * 2354. Number of Excellent Pairs
 * Hard
 *
 * You are given a 0-indexed positive integer array nums and a positive integer k.
 *
 * A pair of numbers (num1, num2) is called excellent if the following conditions are satisfied:
 *
 * Both the numbers num1 and num2 exist in the array nums.
 * The sum of the number of set bits in num1 OR num2 and num1 AND num2 is greater than or equal to k, where OR is the bitwise OR operation and AND is the bitwise AND operation.
 *
 * Return the number of distinct excellent pairs.
 *
 * Two pairs (a, b) and (c, d) are considered distinct if either a != c or b != d. For example, (1, 2) and (2, 1) are distinct.
 *
 * Note that a pair (num1, num2) such that num1 == num2 can also be excellent if you have at least one occurrence of num1 in the array.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1], k = 3
 * Output: 5
 * Explanation: The excellent pairs are the following:
 * - (3, 3). (3 AND 3) and (3 OR 3) are both equal to (11) in binary. The total number of set bits is 2 + 2 = 4, which is greater than or equal to k = 3.
 * - (2, 3) and (3, 2). (2 AND 3) is equal to (10) in binary, and (2 OR 3) is equal to (11) in binary. The total number of set bits is 1 + 2 = 3.
 * - (1, 3) and (3, 1). (1 AND 3) is equal to (01) in binary, and (1 OR 3) is equal to (11) in binary. The total number of set bits is 1 + 2 = 3.
 * So the number of excellent pairs is 5.
 *
 * Example 2:
 *
 * Input: nums = [5,1,1], k = 10
 * Output: 0
 * Explanation: There are no excellent pairs for this array.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i] <= 10^9
 * 1 <= k <= 60
 *
 */
public class NumberOfExcellentPairs {

    // V0
    // IDEA: BIT COUNT IDENTITY + SUFFIX COUNTS OVER POPCOUNT BUCKETS
    //   bits(a | b) + bits(a & b) == bits(a) + bits(b): each bit set in both a and b is
    //   counted once by OR and once by AND, a bit set in only one is counted once by OR.
    //   so the condition is bits(a) + bits(b) >= k and only popcounts matter.
    //   dedupe nums (pairs are of distinct values, ordered, and (x, x) is allowed),
    //   bucket by popcount, and for bucket b add count[b] * (#values with popcount >= k - b).
    /**
     * time = O(N)
     * space = O(N)
     */
    public long countExcellentPairs(int[] nums, int k) {
        // nums[i] <= 10^9 < 2^30, so a popcount is at most 30
        final int maxBits = 32;

        Set<Integer> distinct = new HashSet<>();
        for (int num : nums) {
            distinct.add(num);
        }

        long[] bucket = new long[maxBits + 1];
        for (int value : distinct) {
            bucket[Integer.bitCount(value)]++;
        }

        // atLeast[i] = number of distinct values whose popcount is >= i
        long[] atLeast = new long[maxBits + 2];
        for (int i = maxBits; i >= 0; i--) {
            atLeast[i] = atLeast[i + 1] + bucket[i];
        }

        long pairs = 0;
        for (int bits = 0; bits <= maxBits; bits++) {
            if (bucket[bits] == 0) {
                continue;
            }
            // the partner needs at least this many set bits
            int need = Math.max(0, k - bits);
            if (need > maxBits) {
                continue;
            }
            pairs += bucket[bits] * atLeast[need];
        }
        return pairs;
    }
}
