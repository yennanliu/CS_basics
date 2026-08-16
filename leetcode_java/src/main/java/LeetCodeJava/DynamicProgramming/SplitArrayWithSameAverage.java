package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/split-array-with-same-average/description/

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

/**
 * 805. Split Array With Same Average
 * Hard
 *
 * You are given an integer array nums.
 *
 * You should move each element of nums into one of the two arrays A and B such that A
 * and B are non-empty, and average(A) == average(B).
 *
 * Return true if it is possible to achieve that and false otherwise.
 *
 * Note that for an array arr, average(arr) is the sum of all the elements of arr over
 * the length of arr.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5,6,7,8]
 * Output: true
 * Explanation: We can split the array into [1,4,5,8] and [2,3,6,7], and both of them
 * have an average of 4.5.
 *
 * Example 2:
 *
 * Input: nums = [3,1]
 * Output: false
 *
 * Constraints:
 *
 * 1 <= nums.length <= 30
 * 0 <= nums[i] <= 10^4
 *
 */
public class SplitArrayWithSameAverage {

    // V0
    // IDEA: MATH REWRITE + MEET IN THE MIDDLE
    /**
     *   Let s = sum(nums), n = nums.length. If A has sum s1 and size k, then
     *
     *       s1 / k == (s - s1) / (n - k)   <=>   s1 / k == s / n
     *
     *   So we just need SOME non-empty PROPER subset whose average equals the
     *   GLOBAL average. To avoid floats, SCALE every element:
     *
     *       a[i] = nums[i] * n - s
     *
     *   Now the task becomes: is there a non-empty proper subset of `a` summing to 0 ?
     *   (note sum(a) == 0, so the COMPLEMENT also sums to 0)
     *
     *   n <= 30, so 2^30 brute force is too slow -> SPLIT the array in half and
     *   enumerate 2^15 subset sums on each side (MEET IN THE MIDDLE).
     *
     *   NOTE !!! the `PROPER subset` requirement: we must not take EVERY element.
     *            If a valid A is (all of left + part of right), its complement B lives
     *            ENTIRELY inside the right half and also sums to 0, so it is found by
     *            the right-half scan anyway -> skipping the all-elements combination
     *            never loses an answer.
     *
     *   time  = O(2^(n/2))
     *   space = O(2^(n/2))
     */
    public boolean splitArraySameAverage(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return false;
        }

        int s = 0;
        for (int v : nums) {
            s += v;
        }

        // SCALE: `same average` -> `subset sums to 0`
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = v(nums[i], n, s);
        }

        int mid = n / 2;
        int leftLen = mid;
        int rightLen = n - mid;

        int[] ls = subsetSums(a, 0, leftLen);
        int[] rs = subsetSums(a, mid, rightLen);

        Set<Integer> seen = new HashSet<>();
        for (int mask = 1; mask < (1 << leftLen); mask++) {
            if (ls[mask] == 0) {
                return true;
            }
            seen.add(ls[mask]);
        }

        int fullRight = (1 << rightLen) - 1;
        for (int mask = 1; mask <= fullRight; mask++) {
            int t = rs[mask];
            if (t == 0) {
                return true;
            }
            /** NOTE !!!
             *
             *  pairing a FULL right half with a FULL left half would take
             *  every element, leaving B empty -> that combination is excluded
             */
            if (mask != fullRight && seen.contains(-t)) {
                return true;
            }
        }

        return false;
    }

    private int v(int num, int n, int s) {
        return num * n - s;
    }

    /** sums[mask] = sum of the elements of a[from, from+len) selected by `mask` */
    private int[] subsetSums(int[] a, int from, int len) {
        int[] sums = new int[1 << len];
        for (int i = 0; i < len; i++) {
            int bit = 1 << i;
            for (int mask = 0; mask < bit; mask++) {
                sums[mask | bit] = sums[mask] + a[from + i];
            }
        }
        return sums;
    }


    // V1
    // IDEA: SUBSET-SUM DP over (size, sum)
    /**
     *  possible[k] = the set of sums achievable by choosing exactly k elements,
     *  stored as a bitset over the sum axis.
     *
     *  Then A exists iff for some k in [1, n/2] the sum k * total / n is achievable
     *  and divides evenly -- no scaling trick and no meet in the middle.
     *
     *  time  = O(n^2 * sum / 64)
     *  space = O(n * sum / 64)
     */
    public boolean splitArraySameAverage_1(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return false;
        }
        int total = 0;
        for (int v : nums) {
            total += v;
        }

        // possible[k] : bitset of achievable sums using exactly k elements
        java.util.BitSet[] possible = new java.util.BitSet[n / 2 + 1];
        for (int k = 0; k <= n / 2; k++) {
            possible[k] = new java.util.BitSet(total + 1);
        }
        possible[0].set(0);

        for (int v : nums) {
            for (int k = Math.min(n / 2, n) - 1; k >= 0; k--) {
                java.util.BitSet shifted = new java.util.BitSet(total + 1);
                for (int s = possible[k].nextSetBit(0); s >= 0;
                     s = possible[k].nextSetBit(s + 1)) {
                    if (s + v <= total) {
                        shifted.set(s + v);
                    }
                }
                possible[k + 1].or(shifted);
            }
        }

        for (int k = 1; k <= n / 2; k++) {
            if ((long) total * k % n != 0) {
                continue;
            }
            if (possible[k].get(total * k / n)) {
                return true;
            }
        }
        return false;
    }

    // V2
    // IDEA: MEET IN THE MIDDLE on the SCALED values, using maps keyed by size
    /**
     *  Same `subset sums to 0 after scaling` reduction as V0, but the two halves
     *  are indexed by SUBSET SIZE as well, so a left part of size i is only paired
     *  with right parts that keep the total subset proper.
     *
     *  That makes the `not every element` guard structural instead of a special
     *  case on the full mask.
     *
     *  time  = O(2^(n/2) * n)
     *  space = O(2^(n/2))
     */
    public boolean splitArraySameAverage_2(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return false;
        }
        int total = 0;
        for (int v : nums) {
            total += v;
        }
        long[] a = new long[n];
        for (int i = 0; i < n; i++) {
            a[i] = (long) nums[i] * n - total;
        }

        int half = n / 2;
        Map<Long, Set<Integer>> left = new HashMap<>();
        for (int mask = 1; mask < (1 << half); mask++) {
            long sum = 0;
            for (int i = 0; i < half; i++) {
                if (((mask >> i) & 1) == 1) {
                    sum += a[i];
                }
            }
            if (sum == 0) {
                return true;
            }
            left.computeIfAbsent(sum, k -> new HashSet<>()).add(Integer.bitCount(mask));
        }

        int rightLen = n - half;
        for (int mask = 1; mask < (1 << rightLen); mask++) {
            long sum = 0;
            for (int i = 0; i < rightLen; i++) {
                if (((mask >> i) & 1) == 1) {
                    sum += a[half + i];
                }
            }
            if (sum == 0) {
                return true;
            }
            Set<Integer> sizes = left.get(-sum);
            if (sizes == null) {
                continue;
            }
            for (int sz : sizes) {
                // the union must be a PROPER, non-empty subset
                if (sz + Integer.bitCount(mask) < n) {
                    return true;
                }
            }
        }
        return false;
    }

    // V3
    // IDEA: BRUTE FORCE over every subset (tiny n)
    /**
     *  Try all 2^n subsets and compare the two averages directly with cross
     *  multiplication.
     *
     *  Only runs for n <= ~22, but it checks the property the statement states
     *  rather than the scaled reformulation -- the oracle for the reduction.
     *
     *  time  = O(2^n * n)
     *  space = O(1)
     */
    public boolean splitArraySameAverage_3(int[] nums) {
        int n = nums.length;
        int total = 0;
        for (int v : nums) {
            total += v;
        }

        for (int mask = 1; mask < (1 << n) - 1; mask++) {
            int sum = 0;
            int cnt = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    sum += nums[i];
                    cnt += 1;
                }
            }
            // sum / cnt == (total - sum) / (n - cnt)
            if ((long) sum * (n - cnt) == (long) (total - sum) * cnt) {
                return true;
            }
        }
        return false;
    }

}
