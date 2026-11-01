package LeetCodeJava.DynamicProgramming;

// https://leetcode.com/problems/find-sum-of-array-product-of-magical-sequences/description/

import java.util.*;

/**
 * 3539. Find Sum of Array Product of Magical Sequences
 * Hard
 *
 * You are given two integers, m and k, and an integer array nums.
 *
 * A sequence of integers seq is called magical if:
 *
 * seq has a size of m.
 *
 * 0 <= seq[i] < nums.length
 *
 * The binary representation of 2^seq[0] + 2^seq[1] + ... + 2^seq[m - 1] has k set
 * bits.
 *
 * The array product of this sequence is defined as prod(seq) = (nums[seq[0]] *
 * nums[seq[1]] * ... * nums[seq[m - 1]]).
 *
 * Return the sum of the array products for all valid magical sequences.
 *
 * Since the answer may be large, return it modulo 10^9 + 7.
 *
 * A set bit refers to a bit in the binary representation of a number that has a
 * value of 1.
 *
 * Example 1:
 *
 * Input: m = 5, k = 5, nums = [1,10,100,10000,1000000]
 *
 * Output: 991600007
 *
 * Explanation:
 *
 * All permutations of [0, 1, 2, 3, 4] are magical sequences, each with an array
 * product of 10^13.
 *
 * Example 2:
 *
 * Input: m = 2, k = 2, nums = [5,4,3,2,1]
 *
 * Output: 170
 *
 * Explanation:
 *
 * The magical sequences are [0, 1], [0, 2], [0, 3], [0, 4], [1, 0], [1, 2], [1,
 * 3], [1, 4], [2, 0], [2, 1], [2, 3], [2, 4], [3, 0], [3, 1], [3, 2], [3, 4], [4,
 * 0], [4, 1], [4, 2], and [4, 3].
 *
 * Example 3:
 *
 * Input: m = 1, k = 1, nums = [28]
 *
 * Output: 28
 *
 * Explanation:
 *
 * The only magical sequence is [0].
 *
 * Constraints:
 *
 * 1 <= k <= m <= 30
 *
 * 1 <= nums.length <= 50
 *
 * 1 <= nums[i] <= 10^8
 *
 */
public class FindSumOfArrayProductOfMagicalSequences {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: DP OVER INDICES, STATE = (SLOTS USED, CARRY INTO THE CURRENT BIT, SET BITS SO FAR)
    //   a sequence matters only through how many times c_i each index is used:
    //   it contributes nums[i]^c_i, and choosing c_i of the m - used free slots
    //   counts the orderings (the multinomial) for free.
    //   The sum of 2^seq[j] is built bit by bit: handling indices in increasing
    //   order, bit i is final once index i is handled -> it is (c_i + carry) & 1,
    //   and (c_i + carry) >> 1 carries into bit i + 1.
    //   dp[used][carry][bits] += dp[...] * C(m - used, c) * nums[i]^c
    //   answer: used == m and bits + popcount(leftover carry) == k
    /**
     * time = O(N * M^3 * K)
     * space = O(M^2 * K)
     */
    public int magicalSum(int m, int k, int[] nums) {
        // binomial coefficients C(a, b) for a, b <= m
        long[][] comb = new long[m + 1][m + 1];
        for (int a = 0; a <= m; a++) {
            comb[a][0] = 1;
            for (int b = 1; b <= a; b++) {
                comb[a][b] = (comb[a - 1][b - 1] + comb[a - 1][b]) % MOD;
            }
        }

        // dp[used][carry][bits]; carry never exceeds m
        long[][][] dp = new long[m + 1][m + 1][k + 1];
        dp[0][0][0] = 1;

        for (int num : nums) {
            // power[c] = num^c mod MOD
            long[] power = new long[m + 1];
            power[0] = 1;
            for (int c = 1; c <= m; c++) {
                power[c] = power[c - 1] * (num % MOD) % MOD;
            }

            long[][][] next = new long[m + 1][m + 1][k + 1];
            for (int used = 0; used <= m; used++) {
                for (int carry = 0; carry <= m; carry++) {
                    for (int bits = 0; bits <= k; bits++) {
                        long ways = dp[used][carry][bits];
                        if (ways == 0) {
                            continue;
                        }
                        int freeSlots = m - used;
                        // use this index c times
                        for (int c = 0; c <= freeSlots; c++) {
                            int total = c + carry;
                            int newBits = bits + (total % 2);
                            if (newBits > k) {
                                continue;
                            }
                            int newCarry = total / 2;
                            long add = ways * comb[freeSlots][c] % MOD * power[c] % MOD;
                            next[used + c][newCarry][newBits] =
                                    (next[used + c][newCarry][newBits] + add) % MOD;
                        }
                    }
                }
            }
            dp = next;
        }

        // the leftover carry still spells out the high bits of the sum
        long answer = 0;
        for (int carry = 0; carry <= m; carry++) {
            for (int bits = 0; bits <= k; bits++) {
                if (bits + Integer.bitCount(carry) == k) {
                    answer = (answer + dp[m][carry][bits]) % MOD;
                }
            }
        }
        return (int) answer;
    }
}
