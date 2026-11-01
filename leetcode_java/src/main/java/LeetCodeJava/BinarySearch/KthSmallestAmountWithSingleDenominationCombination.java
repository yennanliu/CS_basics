package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/kth-smallest-amount-with-single-denomination-combination/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 3116. Kth Smallest Amount With Single Denomination Combination
 * Hard
 *
 * You are given an integer array coins representing coins of different denominations and an integer k.
 *
 * You have an infinite number of coins of each denomination. However, you are not allowed to combine coins of different denominations.
 *
 * Return the kth smallest amount that can be made using these coins.
 *
 *
 * Example 1:
 *
 * Input: coins = [3,6,9], k = 3
 * Output: 9
 * Explanation: The given coins can make the following amounts:
 * Coin 3 produces multiples of 3: 3, 6, 9, 12, 15, etc.
 * Coin 6 produces multiples of 6: 6, 12, 18, 24, etc.
 * Coin 9 produces multiples of 9: 9, 18, 27, 36, etc.
 * All of the coins combined produce: 3, 6, 9, 12, 15, etc.
 *
 * Example 2:
 *
 * Input: coins = [5,2], k = 7
 * Output: 12
 * Explanation: The given coins can make the following amounts:
 * Coin 5 produces multiples of 5: 5, 10, 15, 20, etc.
 * Coin 2 produces multiples of 2: 2, 4, 6, 8, 10, 12, etc.
 * All of the coins combined produce: 2, 4, 5, 6, 8, 10, 12, 14, 15, etc.
 *
 *
 * Constraints:
 *
 * 1 <= coins.length <= 15
 * 1 <= coins[i] <= 25
 * 1 <= k <= 2 * 10^9
 *
 */
public class KthSmallestAmountWithSingleDenominationCombination {

    // V0
    // IDEA: BINARY SEARCH THE AMOUNT + INCLUSION-EXCLUSION COUNT
    // reachable amounts are the union of each coin's multiples, so
    //   count(x) = sum over non-empty subsets S of (-1)^(|S|+1) * floor(x / lcm(S))
    // (at most 2^15 - 1 subsets). count(x) is non-decreasing, so binary search the smallest x
    // with count(x) >= k. min(coins) * k is a safe upper bound: that coin alone reaches k.
    /**
     * time = O(2^N * log(min(coins) * k))
     * space = O(2^N)
     */
    public long findKthSmallest(int[] coins, int k) {
        int n = coins.length;
        int minCoin = Integer.MAX_VALUE;
        for (int coin : coins) {
            minCoin = Math.min(minCoin, coin);
        }
        long upperBound = (long) minCoin * k;

        // precompute lcm and sign of every non-empty subset (mask bit i = coins[i] is in it)
        List<long[]> subsets = new ArrayList<>();
        for (int mask = 1; mask < (1 << n); mask++) {
            long lcm = 1;
            int members = 0;
            for (int i = 0; i < n; i++) {
                if (((mask >> i) & 1) == 1) {
                    members++;
                    lcm = lcm / gcd(lcm, coins[i]) * coins[i];
                    // an lcm beyond the search range contributes 0 to every count
                    if (lcm > upperBound) {
                        break;
                    }
                }
            }
            if (lcm > upperBound) {
                continue;
            }
            long sign = (members % 2 == 1) ? 1 : -1;
            subsets.add(new long[] {lcm, sign});
        }

        long left = 1;
        long right = upperBound;
        while (left < right) {
            long mid = left + (right - left) / 2;
            if (countReachable(subsets, mid) >= k) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }

    /** How many reachable amounts are <= x (inclusion-exclusion). */
    private long countReachable(List<long[]> subsets, long x) {
        long count = 0;
        for (long[] subset : subsets) {
            count += subset[1] * (x / subset[0]);
        }
        return count;
    }

    /** Euclid's gcd. */
    private long gcd(long a, long b) {
        while (b != 0) {
            long remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
