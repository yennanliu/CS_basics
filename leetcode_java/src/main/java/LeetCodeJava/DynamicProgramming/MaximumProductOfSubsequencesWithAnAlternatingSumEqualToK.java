package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/maximum-product-of-subsequences-with-an-alternating-sum-equal-to-k/description/

import java.util.*;

/**
 * 3509. Maximum Product of Subsequences With an Alternating Sum Equal to K
 * Hard
 *
 * You are given an integer array nums and two integers, k and limit. Your task is to find
 * a non-empty subsequence of nums that:
 *
 * Has an alternating sum equal to k.
 * Maximizes the product of all its numbers without the product exceeding limit.
 *
 * Return the product of the numbers in such a subsequence. If no subsequence satisfies the
 * requirements, return -1.
 *
 * The alternating sum of a 0-indexed array is defined as the sum of the elements at even
 * indices minus the sum of the elements at odd indices.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3], k = 2, limit = 10
 * Output: 6
 * Explanation: [1, 2, 3] has alternating sum 1 - 2 + 3 = 2 and product 6; [2] has product 2.
 *
 * Example 2:
 *
 * Input: nums = [0,2,3], k = -5, limit = 12
 * Output: -1
 *
 * Example 3:
 *
 * Input: nums = [2,2,3,3], k = 0, limit = 9
 * Output: 9
 * Explanation: [2, 2, 3, 3] has product 36 > 9; the next best is [3, 3] with product 9.
 *
 *
 * Constraints:
 *
 * 1 <= nums.length <= 150
 * 0 <= nums[i] <= 12
 * -10^5 <= k <= 10^5
 * 1 <= limit <= 5000
 *
 */
public class MaximumProductOfSubsequencesWithAnAlternatingSumEqualToK {

    // alternating sums lie in [-900, 900] (at most 75 elements of value <= 12 on each parity)
    private static final int OFFSET = 1000;
    private static final int SUM_BITS = 2 * OFFSET + 1;
    private static final int WORDS = (SUM_BITS + 63) / 64;
    // product bucket meaning "already above limit" (a later 0 can still reset it to 0)
    private static final int OVER = -1;

    // V0
    // IDEA: DP KEYED ON (PARITY, PRODUCT) -> BITSET OF REACHABLE ALTERNATING SUMS
    /**
     *  DP def:
     *     - dp[parity].get(product) = bitset of reachable alternating sums (bit OFFSET + s)
     *       over subsequences so far whose LENGTH has that parity
     *
     *  DP eq (appending value v):
     *     - length even -> v lands on an even index -> sum + v -> bitset << v, length becomes odd
     *     - length odd  -> v lands on an odd index  -> sum - v -> bitset >> v, length becomes even
     *     - product' = product * v, capped into the OVER bucket when > limit
     *       (OVER * 0 = 0, so an over-limit product is not dead)
     *     - v can also start a brand-new subsequence (length 1, sum v)
     *
     *  Answer: largest product != OVER whose bitset (either parity) contains k, else -1
     *
     *  time  = O(N * P * S / 64)   P = reachable products <= limit, S = sum range
     *  space = O(P * S / 64)
     */
    public int maxProduct(int[] nums, int k, int limit) {
        // k outside the reachable sum range can never be hit
        if (k <= -OFFSET || k >= OFFSET) {
            return -1;
        }

        // index 0: even length so far, index 1: odd length so far
        List<Map<Integer, long[]>> dp = new ArrayList<>();
        dp.add(new HashMap<>());
        dp.add(new HashMap<>());

        for (int value : nums) {
            // copies: "skip value" keeps every old state
            Map<Integer, long[]> nextEven = copyStates(dp.get(0));
            Map<Integer, long[]> nextOdd = copyStates(dp.get(1));

            // start a new subsequence [value]: odd length, sum = value
            int startProduct = multiply(1, value, limit);
            long[] single = new long[WORDS];
            setBit(single, OFFSET + value);
            orInto(nextOdd, startProduct, single);

            // even length so far -> value is added; result has odd length
            for (Map.Entry<Integer, long[]> entry : dp.get(0).entrySet()) {
                int product = multiply(entry.getKey(), value, limit);
                orInto(nextOdd, product, shiftUp(entry.getValue(), value));
            }
            // odd length so far -> value is subtracted; result has even length
            for (Map.Entry<Integer, long[]> entry : dp.get(1).entrySet()) {
                int product = multiply(entry.getKey(), value, limit);
                orInto(nextEven, product, shiftDown(entry.getValue(), value));
            }

            dp.set(0, nextEven);
            dp.set(1, nextOdd);
        }

        int best = -1;
        for (Map<Integer, long[]> states : dp) {
            for (Map.Entry<Integer, long[]> entry : states.entrySet()) {
                int product = entry.getKey();
                if (product != OVER && product > best && getBit(entry.getValue(), OFFSET + k)) {
                    best = product;
                }
            }
        }
        return best;
    }

    /** product * value, with OVER for anything above limit; anything times 0 is 0. */
    private int multiply(int product, int value, int limit) {
        if (value == 0 || product == 0) {
            return 0;
        }
        if (product == OVER) {
            return OVER;
        }
        int result = product * value;
        return result <= limit ? result : OVER;
    }

    /** Shallow map copy with cloned bitsets, so later ORs do not alias the old layer. */
    private Map<Integer, long[]> copyStates(Map<Integer, long[]> states) {
        Map<Integer, long[]> copy = new HashMap<>();
        for (Map.Entry<Integer, long[]> entry : states.entrySet()) {
            copy.put(entry.getKey(), entry.getValue().clone());
        }
        return copy;
    }

    /** states[product] |= bits. */
    private void orInto(Map<Integer, long[]> states, int product, long[] bits) {
        long[] target = states.get(product);
        if (target == null) {
            states.put(product, bits.clone());
            return;
        }
        for (int w = 0; w < WORDS; w++) {
            target[w] |= bits[w];
        }
    }

    /** Every set bit b moves to b + shift (bits past the range are dropped). */
    private long[] shiftUp(long[] bits, int shift) {
        long[] result = new long[WORDS];
        for (int b = SUM_BITS - 1 - shift; b >= 0; b--) {
            if (getBit(bits, b)) {
                setBit(result, b + shift);
            }
        }
        return result;
    }

    /** Every set bit b moves to b - shift (bits below 0 are dropped). */
    private long[] shiftDown(long[] bits, int shift) {
        long[] result = new long[WORDS];
        for (int b = shift; b < SUM_BITS; b++) {
            if (getBit(bits, b)) {
                setBit(result, b - shift);
            }
        }
        return result;
    }

    /** Sets bit index in the packed bitset (word = index / 64, position = index % 64). */
    private void setBit(long[] bits, int index) {
        bits[index / 64] |= 1L << (index % 64);
    }

    /** True if bit index is set in the packed bitset. */
    private boolean getBit(long[] bits, int index) {
        return (bits[index / 64] & (1L << (index % 64))) != 0;
    }
}
