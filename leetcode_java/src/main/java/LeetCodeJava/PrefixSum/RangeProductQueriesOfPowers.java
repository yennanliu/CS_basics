package LeetCodeJava.PrefixSum;

// https://leetcode.com/problems/range-product-queries-of-powers/description/

import java.util.ArrayList;
import java.util.List;

/**
 * 2438. Range Product Queries of Powers
 * Medium
 *
 * Given a positive integer n, there exists a 0-indexed array called powers, composed of the minimum number of powers of 2 that sum to n. The array is sorted in non-decreasing order, and there is only one way to form the array.
 *
 * You are also given a 0-indexed 2D integer array queries, where queries[i] = [lefti, righti]. Each queries[i] represents a query where you have to find the product of all powers[j] with lefti <= j <= righti.
 *
 * Return an array answers, equal in length to queries, where answers[i] is the answer to the ith query. Since the answer to the ith query may be too large, each answers[i] should be returned modulo 10^9 + 7.
 *
 *
 * Example 1:
 *
 * Input: n = 15, queries = [[0,1],[2,2],[0,3]]
 * Output: [2,4,64]
 * Explanation:
 * For n = 15, powers = [1,2,4,8]. It can be shown that powers cannot be a smaller size.
 * Answer to 1st query: powers[0] * powers[1] = 1 * 2 = 2.
 * Answer to 2nd query: powers[2] = 4.
 * Answer to 3rd query: powers[0] * powers[1] * powers[2] * powers[3] = 1 * 2 * 4 * 8 = 64.
 * Each answer modulo 10^9 + 7 yields the same answer, so [2,4,64] is returned.
 *
 * Example 2:
 *
 * Input: n = 2, queries = [[0,0]]
 * Output: [2]
 * Explanation:
 * For n = 2, powers = [2].
 * The answer to the only query is powers[0] = 2. The answer modulo 10^9 + 7 is the same, so [2] is returned.
 *
 *
 * Constraints:
 *
 * 1 <= n <= 10^9
 * 1 <= queries.length <= 10^5
 * 0 <= starti <= endi < powers.length
 *
 */
public class RangeProductQueriesOfPowers {

    private static final int MOD = 1_000_000_007;

    // V0
    // IDEA: BIT DECOMPOSITION + PREFIX SUM OF EXPONENTS
    //       the minimum set of powers of 2 summing to n is exactly n's set bits, in
    //       ascending order -> powers[j] = 2^e[j]. a range product is then
    //       2^(e[l] + ... + e[r]), so keep a prefix sum of exponents and answer
    //       each query with one modular power. n <= 10^9 -> at most 30 powers.
    /**
     * time = O(30 + Q * log(sum of exponents))
     * space = O(30)
     */
    public int[] productQueries(int n, int[][] queries) {
        // exponents of the set bits of n, smallest first
        List<Integer> exponents = new ArrayList<>();
        int remaining = n;
        int bit = 0;
        while (remaining > 0) {
            // lowest bit set -> 2^bit is one of the powers
            if (remaining % 2 == 1) {
                exponents.add(bit);
            }
            remaining /= 2;
            bit++;
        }

        // prefixExponent[i] = e[0] + ... + e[i-1]
        int[] prefixExponent = new int[exponents.size() + 1];
        for (int i = 0; i < exponents.size(); i++) {
            prefixExponent[i + 1] = prefixExponent[i] + exponents.get(i);
        }

        int[] answers = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            int left = queries[q][0];
            int right = queries[q][1];
            int exponentSum = prefixExponent[right + 1] - prefixExponent[left];
            answers[q] = (int) modPow(2, exponentSum);
        }
        return answers;
    }

    /** base^exponent mod MOD by repeated squaring. */
    private long modPow(long base, int exponent) {
        long result = 1;
        long factor = base % MOD;
        while (exponent > 0) {
            if (exponent % 2 == 1) {
                result = result * factor % MOD;
            }
            factor = factor * factor % MOD;
            exponent /= 2;
        }
        return result;
    }
}
