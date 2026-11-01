package LeetCodeJava.BinarySearch;

// https://leetcode.com/problems/find-products-of-elements-of-big-array/description/

/**
 * 3145. Find Products of Elements of Big Array
 * Hard
 *
 * The powerful array of a non-negative integer x is defined as the shortest sorted array of powers of two that sum up to x. The table below illustrates examples of how the powerful array is determined. It can be proven that the powerful array of x is unique.
 *
 *     num     powerful array
 *     1       [1]
 *     8       [8]
 *     10      [2, 8]
 *     13      [1, 4, 8]
 *     23      [1, 2, 4, 16]
 *
 * The array big_nums is created by concatenating the powerful arrays for every positive integer i in ascending order: 1, 2, 3, and so forth. Thus, big_nums begins as [1, 2, 1, 2, 4, 1, 4, 2, 4, 1, 2, 4, 8, ...].
 *
 * You are given a 2D integer matrix queries, where for queries[i] = [from_i, to_i, mod_i] you should calculate (big_nums[from_i] * big_nums[from_i + 1] * ... * big_nums[to_i]) % mod_i.
 *
 * Return an integer array answer such that answer[i] is the answer to the ith query.
 *
 *
 * Example 1:
 *
 * Input: queries = [[1,3,7]]
 * Output: [4]
 * Explanation:
 * There is one query.
 * big_nums[1..3] = [2,1,2]. The product of them is 4. The remainder of 4 under 7 is 4.
 *
 * Example 2:
 *
 * Input: queries = [[2,5,3],[7,7,4]]
 * Output: [2,2]
 * Explanation:
 * There are two queries.
 * First query: big_nums[2..5] = [1,2,4,1]. The product of them is 8. The remainder of 8 under 3 is 2.
 * Second query: big_nums[7] = 2. The remainder of 2 under 4 is 2.
 *
 *
 * Constraints:
 *
 * 1 <= queries.length <= 500
 * queries[i].length == 3
 * 0 <= queries[i][0] <= queries[i][1] <= 10^15
 * 1 <= queries[i][2] <= 10^5
 *
 */
public class FindProductsOfElementsOfBigArray {

    // V0
    // IDEA: EVERY ENTRY IS A POWER OF TWO -> WORK WITH PREFIX SUMS OF EXPONENTS
    // the product of big_nums[from..to] is 2^(g(to + 1) - g(from)), where
    //   g(N) = sum of the exponents of the first N entries of big_nums.
    // g is computed with closed forms over 1..n, using "bit b is set in
    //   (n + 1) / 2^(b+1) * 2^b + max(0, (n + 1) % 2^(b+1) - 2^b)   numbers of 1..n":
    //   entries(n)  = total set bits in 1..n
    //   expSum(n)   = sum of the set-bit positions in 1..n
    // binary search the largest n with entries(n) <= N, then add the lowest set bits of n + 1
    // for the partially used number. mod may be 1 -> the answer is 0.
    /**
     * time = O(Q * log^2(maxIndex))
     * space = O(1)
     */
    public int[] findProductsOfElements(long[][] queries) {
        int[] answer = new int[queries.length];
        for (int q = 0; q < queries.length; q++) {
            long from = queries[q][0];
            long to = queries[q][1];
            long mod = queries[q][2];
            long exponent = exponentSum(to + 1) - exponentSum(from);
            answer[q] = (int) powMod(2, exponent, mod);
        }
        return answer;
    }

    /** How many numbers in 1..n have bit `bit` set. */
    private long countWithBit(long n, int bit) {
        long blockSize = 1L << bit;
        long period = blockSize * 2;
        return (n + 1) / period * blockSize + Math.max(0, (n + 1) % period - blockSize);
    }

    /** Number of big_nums entries contributed by the numbers 1..n (= total set bits). */
    private long entriesUpTo(long n) {
        long total = 0;
        for (int bit = 0; (1L << bit) <= n; bit++) {
            total += countWithBit(n, bit);
        }
        return total;
    }

    /** Sum of the exponents contributed by the numbers 1..n. */
    private long exponentsUpTo(long n) {
        long total = 0;
        for (int bit = 0; (1L << bit) <= n; bit++) {
            total += bit * countWithBit(n, bit);
        }
        return total;
    }

    /** Sum of the exponents of the first `count` entries of big_nums. */
    private long exponentSum(long count) {
        if (count <= 0) {
            return 0;
        }
        // largest n whose entries all fit; entries(n) >= n, so n <= count
        long left = 0;
        long right = count;
        while (left < right) {
            long mid = left + (right - left + 1) / 2;
            if (entriesUpTo(mid) <= count) {
                left = mid;
            } else {
                right = mid - 1;
            }
        }
        long sum = exponentsUpTo(left);

        // the remaining entries come from the lowest set bits of the number left + 1
        long remaining = count - entriesUpTo(left);
        long value = left + 1;
        int bit = 0;
        while (remaining > 0 && value > 0) {
            if ((value & 1) == 1) {
                sum += bit;
                remaining--;
            }
            value >>= 1;
            bit++;
        }
        return sum;
    }

    /** base^exponent % mod by fast exponentiation. */
    private long powMod(long base, long exponent, long mod) {
        long result = 1 % mod;
        base %= mod;
        while (exponent > 0) {
            if ((exponent & 1) == 1) {
                result = result * base % mod;
            }
            base = base * base % mod;
            exponent >>= 1;
        }
        return result;
    }
}
