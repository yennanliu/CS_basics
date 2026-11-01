package LeetCodeJava.Math;

// https://leetcode.com/problems/sum-of-beautiful-subsequences/description/

import java.util.*;

/**
 * 3671. Sum of Beautiful Subsequences
 * Hard
 *
 * You are given an integer array nums of length n.
 *
 * For every positive integer g, we define the beauty of g as the product of g
 * and the number of strictly increasing subsequences of nums whose greatest
 * common divisor (GCD) is exactly g.
 *
 * Return the sum of beauty values for all positive integers g.
 *
 * Since the answer could be very large, return it modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: nums = [1,2,3]
 * Output: 10
 * Explanation:
 * All strictly increasing subsequences and their GCDs are:
 *
 * Subsequence  GCD
 * [1]          1
 * [2]          2
 * [3]          3
 * [1,2]        1
 * [1,3]        1
 * [2,3]        1
 * [1,2,3]      1
 *
 * Calculating beauty for each GCD:
 *
 * GCD  Count of subsequences  Beauty (GCD x Count)
 * 1    5                      1 x 5 = 5
 * 2    1                      2 x 1 = 2
 * 3    1                      3 x 1 = 3
 *
 * Total beauty is 5 + 2 + 3 = 10.
 *
 * Example 2:
 *
 * Input: nums = [4,6]
 * Output: 12
 * Explanation:
 * All strictly increasing subsequences and their GCDs are:
 *
 * Subsequence  GCD
 * [4]          4
 * [6]          6
 * [4,6]        2
 *
 * Calculating beauty for each GCD:
 *
 * GCD  Count of subsequences  Beauty (GCD x Count)
 * 2    1                      2 x 1 = 2
 * 4    1                      4 x 1 = 4
 * 6    1                      6 x 1 = 6
 *
 * Total beauty is 2 + 4 + 6 = 12.
 *
 *
 * Constraints:
 *
 * 1 <= n == nums.length <= 10^4
 * 1 <= nums[i] <= 7 * 10^4
 *
 */
public class SumOfBeautifulSubsequences {

    private static final long MOD = 1_000_000_007L;

    // V0
    // IDEA: "GCD DIVISIBLE BY h" IS EASY; EULER'S PHI TURNS IT INTO "GCD EXACTLY g" FOR FREE
    //       let F(h) = number of strictly increasing subsequences using only multiples of h.
    //       mobius inversion gives E(g) (gcd exactly g), and sum_g g * E(g) regroups into
    //       sum_h F(h) * sum_{g | h} g * mu(h / g) = sum_h phi(h) * F(h)    (Id * mu = phi).
    //       F(h): walk the multiples of h in ORIGINAL order (divided by h); each element x gets
    //       dp = 1 + (sum of dp over earlier elements with a smaller value), via a Fenwick tree
    //       over the compressed values. total work ~ sum of divisor counts of the elements.
    /**
     * time = O(V log V + D log N)    (V = max value, D = sum of divisor counts)
     * space = O(V + D)
     */
    public int totalBeauty(int[] nums) {
        int maxValue = 0;
        for (int num : nums) {
            maxValue = Math.max(maxValue, num);
        }

        boolean[] present = new boolean[maxValue + 1];
        for (int num : nums) {
            present[num] = true;
        }

        // divisors[x] = all divisors of x, only for values that occur
        List<List<Integer>> divisors = new ArrayList<>();
        for (int x = 0; x <= maxValue; x++) {
            divisors.add(present[x] ? new ArrayList<>() : null);
        }
        for (int d = 1; d <= maxValue; d++) {
            for (int multiple = d; multiple <= maxValue; multiple += d) {
                if (present[multiple]) {
                    divisors.get(multiple).add(d);
                }
            }
        }

        // euler's totient sieve: phi[j] *= (1 - 1/p) for every prime p dividing j
        int[] phi = new int[maxValue + 1];
        for (int i = 0; i <= maxValue; i++) {
            phi[i] = i;
        }
        for (int p = 2; p <= maxValue; p++) {
            if (phi[p] == p) { // untouched so far -> p is prime
                for (int j = p; j <= maxValue; j += p) {
                    phi[j] -= phi[j] / p;
                }
            }
        }

        // groups[h] = the multiples of h, divided by h, in original order
        List<List<Integer>> groups = new ArrayList<>();
        for (int h = 0; h <= maxValue; h++) {
            groups.add(null);
        }
        for (int num : nums) {
            for (int d : divisors.get(num)) {
                if (groups.get(d) == null) {
                    groups.set(d, new ArrayList<>());
                }
                groups.get(d).add(num / d);
            }
        }

        long answer = 0;
        for (int h = 1; h <= maxValue; h++) {
            List<Integer> values = groups.get(h);
            if (values != null) {
                answer = (answer + (long) phi[h] * countIncreasing(values)) % MOD;
            }
        }
        return (int) answer;
    }

    /** Number of strictly increasing subsequences of values, modulo MOD. */
    private long countIncreasing(List<Integer> values) {
        // compress the values to ranks 1..size
        TreeSet<Integer> distinct = new TreeSet<>(values);
        Map<Integer, Integer> rank = new HashMap<>();
        int nextRank = 1;
        for (int value : distinct) {
            rank.put(value, nextRank++);
        }
        int size = distinct.size();

        // tree: Fenwick tree over ranks, holding sum of dp for each value seen so far
        long[] tree = new long[size + 1];
        long total = 0;
        for (int value : values) {
            int position = rank.get(value);

            // subsequences ending at an earlier, strictly smaller value: prefix sum up to rank - 1
            long smaller = 0;
            for (int i = position - 1; i > 0; i -= i & -i) { // i & -i = lowest set bit of i
                smaller = (smaller + tree[i]) % MOD;
            }
            // extend each of those, or start a new subsequence with this value alone
            long endingHere = (smaller + 1) % MOD;
            total = (total + endingHere) % MOD;

            for (int i = position; i <= size; i += i & -i) {
                tree[i] = (tree[i] + endingHere) % MOD;
            }
        }
        return total;
    }
}
