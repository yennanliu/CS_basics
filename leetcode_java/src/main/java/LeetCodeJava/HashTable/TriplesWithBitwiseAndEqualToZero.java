package LeetCodeJava.HashTable;

// https://leetcode.com/problems/triples-with-bitwise-and-equal-to-zero/description/

import java.util.HashMap;
import java.util.Map;
/**
 * 982. Triples with Bitwise AND Equal To Zero
 * Hard
 *
 * Given an integer array nums, return the number of AND triples.
 *
 * An AND triple is a triple of indices (i, j, k) such that:
 *
 * 0 <= i < nums.length
 * 0 <= j < nums.length
 * 0 <= k < nums.length
 * nums[i] & nums[j] & nums[k] == 0, where & represents the bitwise-AND operator.
 *
 * Example 1:
 *
 * Input: nums = [2,1,3]
 * Output: 12
 * Explanation: We could choose the following i, j, k triples:
 * (i=0, j=0, k=1) : 2 & 2 & 1
 * (i=0, j=1, k=0) : 2 & 1 & 2
 * (i=0, j=1, k=1) : 2 & 1 & 1
 * (i=0, j=1, k=2) : 2 & 1 & 3
 * (i=0, j=2, k=1) : 2 & 3 & 1
 * (i=1, j=0, k=0) : 1 & 2 & 2
 * (i=1, j=0, k=1) : 1 & 2 & 1
 * (i=1, j=0, k=2) : 1 & 2 & 3
 * (i=1, j=1, k=0) : 1 & 1 & 2
 * (i=1, j=2, k=0) : 1 & 3 & 2
 * (i=2, j=0, k=1) : 3 & 2 & 1
 * (i=2, j=1, k=0) : 3 & 1 & 2
 *
 * Example 2:
 *
 * Input: nums = [0,0,0]
 * Output: 27
 *
 * Constraints:
 *
 * 1 <= nums.length <= 1000
 * 0 <= nums[i] < 2^16
 *
 */
public class TriplesWithBitwiseAndEqualToZero {

    // V0
    // IDEA: BUCKET COUNT of PAIRWISE ANDs
    /**
     *  - Brute force over (i, j, k) is O(n^3) = 10^9 -> TOO SLOW.
     *
     *  - But nums[i] < 2^16, so there are at most 65536 DISTINCT values of
     *    (nums[i] & nums[j]). BUCKET all n^2 pairs by their AND value first,
     *    then for each nums[k] sum the buckets that AND to 0 with it.
     *
     *  - The triple is ORDERED (i, j, k range independently), so NO
     *    de-duplication is needed.
     *
     *  NOTE !!! a plain int[1 << 16] beats a HashMap here -- the key space is
     *           small and dense, and the inner loop runs n * 2^16 times.
     *
     *  time  = O(n^2 + n * 2^16)
     *  space = O(2^16)
     */
    public int countTriplets(int[] nums) {
        final int SIZE = 1 << 16;

        // cnt[v] = how many ORDERED pairs (i, j) have nums[i] & nums[j] == v
        int[] cnt = new int[SIZE];
        for (int a : nums) {
            for (int b : nums) {
                cnt[a & b] += 1;
            }
        }

        int res = 0;
        for (int c : nums) {
            for (int v = 0; v < SIZE; v++) {
                if (cnt[v] != 0 && (v & c) == 0) {
                    res += cnt[v];
                }
            }
        }

        return res;
    }


    // V1
    // IDEA: SUM OVER SUBSETS (SOS DP) -- drop the inner 2^16 scan
    /**
     *  V0 pairs every nums[k] against all 65536 buckets. Instead precompute
     *
     *      f[m] = sum of cnt[v] over all v that are SUBSETS of m
     *
     *  with the standard SOS dynamic programming (one pass per bit). Then the
     *  answer for a given c is just f[~c], because `v & c == 0` is exactly
     *  `v is a subset of ~c`.
     *
     *  -> O(2^16 * 16 + n) instead of O(n * 2^16).
     *
     *  time  = O(n^2 + 2^16 * 16)
     *  space = O(2^16)
     */
    public int countTriplets_1(int[] nums) {
        final int BITS = 16;
        final int SIZE = 1 << BITS;

        int[] f = new int[SIZE];
        for (int a : nums) {
            for (int b : nums) {
                f[a & b] += 1;
            }
        }

        // SOS: after bit i, f[m] holds the sum over subsets differing only in bits <= i
        for (int i = 0; i < BITS; i++) {
            for (int m = 0; m < SIZE; m++) {
                if ((m & (1 << i)) != 0) {
                    f[m] += f[m ^ (1 << i)];
                }
            }
        }

        int res = 0;
        for (int c : nums) {
            res += f[(~c) & (SIZE - 1)];
        }
        return res;
    }

    // V2
    // IDEA: HASH MAP BUCKETS (sparse rather than dense)
    /**
     *  Only the AND values that actually OCCUR need a bucket, and for small n that
     *  is far fewer than 65536.
     *
     *  -> the inner loop runs over the map's ENTRY SET instead of the full value
     *     space, so the cost tracks the data rather than the constraint.
     *
     *  time  = O(n^2 + n * distinct ANDs)
     *  space = O(distinct ANDs)
     */
    public int countTriplets_2(int[] nums) {
        Map<Integer, Integer> cnt = new HashMap<>();
        for (int a : nums) {
            for (int b : nums) {
                cnt.merge(a & b, 1, Integer::sum);
            }
        }

        int res = 0;
        for (int c : nums) {
            for (Map.Entry<Integer, Integer> e : cnt.entrySet()) {
                if ((e.getKey() & c) == 0) {
                    res += e.getValue();
                }
            }
        }
        return res;
    }

    // V3
    // IDEA: BRUTE FORCE over all ordered triples
    /**
     *  Three nested loops, exactly as the statement defines an AND triple.
     *
     *  O(n^3) = 10^9 at n = 1000 so it is far too slow to submit, but for small
     *  inputs it settles any argument about the bucketing versions.
     *
     *  time  = O(n^3)
     *  space = O(1)
     */
    public int countTriplets_3(int[] nums) {
        int n = nums.length;
        int res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int ab = nums[i] & nums[j];
                if (ab == 0) {
                    res += n; // every k works
                    continue;
                }
                for (int k = 0; k < n; k++) {
                    if ((ab & nums[k]) == 0) {
                        res += 1;
                    }
                }
            }
        }
        return res;
    }

}
