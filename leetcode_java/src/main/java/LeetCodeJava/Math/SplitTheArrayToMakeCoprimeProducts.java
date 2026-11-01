package LeetCodeJava.Math;

// https://leetcode.com/problems/split-the-array-to-make-coprime-products/description/

import java.util.HashMap;
import java.util.Map;

/**
 * 2584. Split the Array to Make Coprime Products
 * Hard
 *
 * You are given a 0-indexed integer array nums of length n.
 *
 * A split at an index i where 0 <= i <= n - 2 is called valid if the product of the first i + 1
 * elements and the product of the remaining elements are coprime.
 *
 * For example, if nums = [2, 3, 3], then a split at the index i = 0 is valid because 2 and 9 are
 * coprime, while a split at the index i = 1 is not valid because 6 and 3 are not coprime.
 * A split at the index i = 2 is not valid because i == n - 1.
 *
 * Return the smallest index i at which the array can be split validly or -1 if there is no such split.
 *
 * Two values val1 and val2 are coprime if gcd(val1, val2) == 1 where gcd(val1, val2) is the greatest
 * common divisor of val1 and val2.
 *
 *
 * Example 1:
 *
 * Input: nums = [4,7,8,15,3,5]
 * Output: 2
 *
 * Example 2:
 *
 * Input: nums = [4,7,15,8,3,5]
 * Output: -1
 *
 *
 * Constraints:
 *
 * n == nums.length
 * 1 <= n <= 10^4
 * 1 <= nums[i] <= 10^6
 *
 */
public class SplitTheArrayToMakeCoprimeProducts {

    // V0
    // IDEA: PRIME FACTORIZATION -> INTERVAL MERGING
    //       each prime p "glues" [first index of p, last index of p]; a split after i is valid
    //       iff no glued interval crosses it, i.e. the running max reach equals i
    /**
     * time = O(N * sqrt(M))
     * space = O(N + number of distinct primes)
     */
    public int findValidSplit(int[] nums) {
        int n = nums.length;
        if (n < 2) {
            return -1;
        }

        // firstIndex[p] = first index whose value has prime factor p
        Map<Integer, Integer> firstIndex = new HashMap<>();
        // reach[i] = furthest index glued to i by a prime that FIRST appears at i
        int[] reach = new int[n];
        for (int i = 0; i < n; i++) {
            reach[i] = i;
        }

        for (int i = 0; i < n; i++) {
            int value = nums[i];
            // trial division: after p * p > value, what is left (> 1) is itself prime
            for (int p = 2; (long) p * p <= value; p++) {
                if (value % p == 0) {
                    markPrime(p, i, firstIndex, reach);
                    while (value % p == 0) {
                        value /= p;
                    }
                }
            }
            if (value > 1) {
                markPrime(value, i, firstIndex, reach);
            }
        }

        // sweep: maxReach is the furthest index any interval started so far extends to
        int maxReach = 0;
        for (int i = 0; i < n - 1; i++) {
            maxReach = Math.max(maxReach, reach[i]);
            if (maxReach == i) {
                return i;
            }
        }
        return -1;
    }

    /** Records that prime p appears at index i, extending the interval that starts at p's first index. */
    private void markPrime(int p, int i, Map<Integer, Integer> firstIndex, int[] reach) {
        if (firstIndex.containsKey(p)) {
            int start = firstIndex.get(p);
            reach[start] = Math.max(reach[start], i);
        } else {
            firstIndex.put(p, i);
        }
    }
}
