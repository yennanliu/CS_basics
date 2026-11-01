package LeetCodeJava.Math;

// https://leetcode.com/problems/count-array-pairs-divisible-by-k/description/

import java.util.*;

/**
 * 2183. Count Array Pairs Divisible by K
 * Hard
 *
 * Given a 0-indexed integer array nums of length n and an integer k, return the number of pairs (i, j) such that:
 *
 * 0 <= i < j <= n - 1 and
 * nums[i] * nums[j] is divisible by k.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,4,5], k = 2
 * Output: 7
 * Explanation:
 * The 7 pairs of indices whose corresponding products are divisible by 2 are
 * (0, 1), (0, 3), (1, 2), (1, 3), (1, 4), (2, 3), and (3, 4).
 * Their products are 2, 4, 6, 8, 10, 12, and 20 respectively.
 * Other pairs such as (0, 2) and (2, 4) have products 3 and 15 respectively, which are not divisible by 2.
 *
 * Example 2:
 *
 * Input: nums = [1,2,3,4], k = 5
 * Output: 0
 * Explanation: There does not exist any pair of indices whose corresponding product is divisible by 5.
 *
 * Constraints:
 *
 * 1 <= nums.length <= 10^5
 * 1 <= nums[i], k <= 10^5
 *
 */
public class CountArrayPairsDivisibleByK {

    // V0
    // IDEA: GCD BUCKETS - ONLY gcd(nums[i], k) MATTERS FOR DIVISIBILITY BY k
    //   nums[i] * nums[j] % k == 0  <=>  g_i * g_j % k == 0, where g = gcd(num, k).
    //   every g is a divisor of k (at most 128 of them for k <= 10^5),
    //   so bucket the array by g and test every pair of buckets.
    /**
     * time = O(n log k + d(k)^2)
     * space = O(d(k))
     */
    public long countPairs(int[] nums, int k) {
        // bucket the numbers by gcd(num, k)
        Map<Integer, Long> countByGcd = new HashMap<>();
        for (int num : nums) {
            int g = gcd(num, k);
            countByGcd.put(g, countByGcd.getOrDefault(g, 0L) + 1);
        }

        List<Integer> divisors = new ArrayList<>(countByGcd.keySet());
        long pairs = 0;
        for (int i = 0; i < divisors.size(); i++) {
            long a = divisors.get(i);
            long countA = countByGcd.get(divisors.get(i));

            // pairs inside the same bucket: choose 2 of countA
            if ((a * a) % k == 0) {
                pairs += countA * (countA - 1) / 2;
            }

            // pairs across two different buckets: each counted once since j > i
            for (int j = i + 1; j < divisors.size(); j++) {
                long b = divisors.get(j);
                if ((a * b) % k == 0) {
                    pairs += countA * countByGcd.get(divisors.get(j));
                }
            }
        }
        return pairs;
    }

    /** Euclidean algorithm. */
    private int gcd(int a, int b) {
        while (b != 0) {
            int remainder = a % b;
            a = b;
            b = remainder;
        }
        return a;
    }
}
